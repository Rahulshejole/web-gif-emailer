package com.mycompany.gifemailer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelManager {

    // Reads all emails from the first column of the Excel file
    public static List<String> readEmailsFromExcel(File file) throws IOException {
        List<String> emails = new ArrayList<>();
        
        // Check if file exists to prevent crash
        if (!file.exists()) {
            return emails;
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell = row.getCell(0); // Read first column (Column A)
                if (cell != null) {
                    String email = cell.toString().trim();
                    if (!email.isEmpty() && email.contains("@")) {
                        emails.add(email);
                    }
                }
            }
        }
        return emails;
    }

    // Appends a new email to the end of the list
    public static void addEmailToExcel(File file, String newEmail) throws IOException {
        Workbook workbook;
        Sheet sheet;

        // 1. Open existing file or create new workbook
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Emails");
        }

        // 2. Find the next empty row
        int lastRow = sheet.getLastRowNum();
        if (lastRow == 0 && sheet.getRow(0) == null) {
             // Handle completely empty sheet
             lastRow = -1;
        }
        
        Row row = sheet.createRow(lastRow + 1);
        Cell cell = row.createCell(0);
        cell.setCellValue(newEmail);

        // 3. Save changes
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}