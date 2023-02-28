package com.lmzz.controller;

import com.lmzz.pojo.Student;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PoiDemoController {

    @PostMapping("/importExcel")
    public static void importExcel(MultipartFile file) throws IOException, ParseException {
        InputStream inputStream = file.getInputStream();

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.getSheetAt(0);

        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i + 2);
            List<String> cellList = new ArrayList<>();
            if (row == null) {
                break;
            }
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cellList.add(getCellValue(row.getCell(j)));
            }

            Student student = new Student();
            student.setName(cellList.get(0));
            student.setAge(Integer.valueOf(cellList.get(1)));
            student.setAddress(cellList.get(2));
            student.setBirthday(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(cellList.get(3)));
            student.setHeight(Double.valueOf(cellList.get(4)));
            student.setIsMainlandChina(Boolean.valueOf(cellList.get(5)));

            studentList.add(student);
        }
        System.out.println(studentList);


    }

    private static String getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                } else {
                    double cellValue = cell.getNumericCellValue();
                    BigDecimal bigDecimal = new BigDecimal(cellValue);
                    String s = bigDecimal.toString();
                    return s;
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }


}
