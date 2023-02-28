package com.lmzz.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class PoiImportTest {
    public static void main(String[] args) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new ClassPathResource("student_info.xlsx").getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                CellType cellType = cell.getCellType();
                if (CellType.NUMERIC.equals(cellType)) {
                    System.out.print(cell.getNumericCellValue() + "\t");
                } else if (CellType.BOOLEAN.equals(cellType)) {
                    System.out.print(cell.getBooleanCellValue() + "\t");
                } else if (CellType.STRING.equals(cellType)) {
                    System.out.print(cell.getStringCellValue() + "\t");
                }
            }
            System.out.println("\n------------------------------------------");

        }
        System.out.println("============================");
        sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                CellType cellType = cell.getCellType();
                if (CellType.NUMERIC.equals(cellType)) {
                    System.out.print(cell.getNumericCellValue() + "\t");
                } else if (CellType.STRING.equals(cellType)) {
                    System.out.print(cell.getStringCellValue() + "\t");
                }
            }
            System.out.println("\n------------------------------------------");
        }
    }
}

