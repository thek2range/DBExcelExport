package com.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DataExportService {
    public static String getReport(ResultSet resultSet,String exportFileLocation,String exportKey) throws Exception  { 
    	String exportFileName = exportFileLocation + File.separator+ System.currentTimeMillis() + ".xls";
    	Date d = new Date(); 
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy"); 
    	String date = sdf.format(d);

    	@SuppressWarnings("resource")
        Workbook writeWorkbook = new HSSFWorkbook();
        Sheet desSheet = writeWorkbook.createSheet( String.valueOf( d ).substring(0,10) );


        try{
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            Row desRow1 = desSheet.createRow(0);
            for(int col=0 ;col < columnsNumber;col++) {
                Cell newpath = desRow1.createCell(col);
                newpath.setCellValue(rsmd.getColumnLabel(col+1));
            }

            int totalRows = 0;

            while(resultSet.next()) {
            	
                //System.out.println("INFO : Row number" + resultSet.getRow() );
                Row desRow = desSheet.createRow(resultSet.getRow());
                for(int col=0 ;col < columnsNumber;col++) {
                    Cell newpath = desRow.createCell(col);
                    if ((col+1) != 4) 
                    	newpath.setCellValue(resultSet.getString(col+1)); 
                    else
                    	newpath.setCellValue(EncryptDecryptString.decrypt(resultSet.getString(col+1),exportKey));                      
                }
                FileOutputStream fileOut = new FileOutputStream( exportFileName );
                writeWorkbook.write(fileOut);
                fileOut.close();
                totalRows++;
            }
            System.out.println("INFO : Total number of rows printed : " + totalRows);
        }
        catch (SQLException e) {
            System.out.println("ERROR : Failed to get data from database");
            e.printStackTrace();
            return null;
        }
        return exportFileName;

    }
 }