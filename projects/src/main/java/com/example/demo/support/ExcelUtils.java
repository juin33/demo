package com.example.demo.support;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author kejun
 * @date 2019/1/4 上午10:22
 */
public class ExcelUtils {
    public static void read() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(ResourceUtils.getFile("/Users/juin/Desktop/read.xlsx")));
        XSSFSheet sheet = workbook.getSheetAt(0);
        for(int i=0;i<sheet.getLastRowNum()+1;i++){
            XSSFRow row = sheet.getRow(i);
            System.out.println("key: "+row.getCell(0).getStringCellValue());
            System.out.println("value: "+row.getCell(1).getStringCellValue());
        }
    }
}
