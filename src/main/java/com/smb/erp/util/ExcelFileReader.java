/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Burhani152
 */
public class ExcelFileReader {

    public static void main(String[] args) {
        //File file = new File("C:\\Users\\Burhani152\\Desktop\\Small Outstanding.xlsx");   //creating a new file instance 
        File file = new File("C:\\Users\\FatemaLaptop\\Desktop\\Payment Report.xlsx");   //creating a new file instance 
        String ext = getFileExtension(file);
        if (ext.equalsIgnoreCase(".xls")) {
            readXlsFile(file);
        } else if (ext.equalsIgnoreCase(".xlsx")) {
            readXlsxFile(file);
        }
    }

    public static List<Row> readFile(File file) {
        String ext = getFileExtension(file);
        if (ext.equalsIgnoreCase(".xls")) {
            return readXlsFile(file);
        } else if (ext.equalsIgnoreCase(".xlsx")) {
            return readXlsxFile(file);
        }
        return null;
    }

    public static List<Row> readXlsxFile(File file) {
        List<Row> rows = new LinkedList<>();
        try {
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    cell.setCellType(CellType.STRING);
                    //if (cell.getCellType() == CellType.STRING) {
                    //    System.out.print(cell.getStringCellValue() + "\t\t\t");
                    //} else if (cell.getCellType() == CellType.NUMERIC) {
                    //    System.out.print(cell.getNumericCellValue() + "\t\t\t");
                    //}
                }
                rows.add(row);
                //System.out.println();
            }
            //System.out.println("Total Rows Read: " + rows.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return rows;
        }
    }

    public static List<Row> readXlsFile(File file) {
        List<Row> rows = new LinkedList<>();
        try {
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating workbook instance that refers to .xls file  
            HSSFWorkbook wb = new HSSFWorkbook(fis);
            //creating a Sheet object to retrieve the object  
            HSSFSheet sheet = wb.getSheetAt(0);
            //evaluating cell type   
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

            for (Row row : sheet) //iteration over row using for each loop  
            {
                for (Cell cell : row) //iteration over cell using for each loop  
                {
                    CellType ctype = formulaEvaluator.evaluateInCell(cell).getCellType();
                    cell.setCellType(CellType.STRING);
                    //if (ctype == CellType.STRING) {     //field that represents numeric cell type  
                        //getting the value of the cell as a number  
                    //    System.out.print(cell.getStringCellValue() + "\t\t");
                    //} else if (ctype == CellType.NUMERIC) {   //getting the value of the cell as a number  
                        //getting the value of the cell as a string  
                    //    System.out.print(cell.getNumericCellValue() + "\t\t");
                    //}
                }
                rows.add(row);
                //System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return rows;
        }
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

}
