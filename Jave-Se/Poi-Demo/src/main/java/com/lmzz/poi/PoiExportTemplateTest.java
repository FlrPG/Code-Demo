package com.lmzz.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PoiExportTemplateTest {

    public static void main(String[] args) throws ParseException, IOException {

        // 构造假数据
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1L, "周星驰", 58, "香港", new SimpleDateFormat("yyyy-MM-dd").parse("1962-6-22"), 174.0, false));
        studentList.add(new Student(2L, "李健", 46, "哈尔滨", new SimpleDateFormat("yyyy-MM-dd").parse("1974-9-23"), 174.5, true));
        studentList.add(new Student(3L, "周深", 28, "贵州", new SimpleDateFormat("yyyy-MM-dd").parse("1992-9-29"), 161.0, true));

        //读取模板
        XSSFWorkbook workbook = new XSSFWorkbook(new ClassPathResource("student_info_template.xlsx").getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFRow templateRow = sheet.getRow(2);
        CellStyle[] templateStyleArr = new CellStyle[templateRow.getLastCellNum()];
        for (int i = 0; i < templateStyleArr.length; i++) {
            templateStyleArr[i] = templateRow.getCell(i).getCellStyle();
        }
        for (int i = 0; i < studentList.size(); i++) {
            XSSFRow row = sheet.createRow(i + 2);
            Student student = studentList.get(i);


            XSSFCell cell = row.createCell(0);
            cell.setCellStyle(templateStyleArr[0]);
            cell.setCellValue(student.getName());

            XSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(templateStyleArr[1]);
            cell1.setCellValue(student.getAge());
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(templateStyleArr[2]);
            cell2.setCellValue(student.getAddress());
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(templateStyleArr[3]);
            cell3.setCellValue(student.getBirthday());
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(templateStyleArr[4]);
            cell4.setCellValue(student.getHeight());
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellStyle(templateStyleArr[5]);
            cell5.setCellValue(student.getIsMainlandChina());

        }

        String path = ResourceUtils.getURL("classpath:").getPath();
        File file = new File(path + "export_template.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        workbook.close();
    }

    @Data
    @AllArgsConstructor
    static class Student {
        private Long id;
        private String name;
        private Integer age;
        private String address;
        private Date birthday;
        private Double height;
        private Boolean isMainlandChina;
    }
}
