package com.newcore.orbps.web.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

public class ExcelToCsv {
	private static Logger logger = LoggerFactory.getLogger(ExcelToCsv.class);
	
	private ExcelToCsv(){
	}
	
    @SuppressWarnings("deprecation")
    public static void process(String excelPath,String csvPath) throws IOException {

    	//read the contents from the excel file
        Workbook book = getExcelWorkbook(excelPath);
        Sheet sheet = getSheetByNum(book,0);
        int lastRowNum = sheet.getLastRowNum();
        StringBuilder csvBuffer = new StringBuilder();
        StringBuilder rowBuffer = new StringBuilder();
        int lastCellNum=0;
    	String value;
        for(int i = 0 ; i <= lastRowNum ; i++){
            Row row = sheet.getRow(i);
            if( row != null ){
                if(i==0){
                    lastCellNum = row.getLastCellNum();                	
                }
                Cell cell;
                rowBuffer=rowBuffer.delete(0, rowBuffer.length());
                for( int j = 0 ; j < lastCellNum ; j++ ){
                    cell = row.getCell(j);
                	value="";
                    if( cell != null ){
                        switch (cell.getCellType()){
                        case HSSFCell.CELL_TYPE_STRING:
                        	value = cell.getStringCellValue().trim();
                        	break;
                        case HSSFCell.CELL_TYPE_NUMERIC:                  	
                        	if (HSSFDateUtil.isCellDateFormatted(cell)) {                        		
                        		Date date = cell.getDateCellValue();
                        		if (date != null) {
                        			value = new SimpleDateFormat("yyyy/M/d").format(date);
                        		} else {
                        			value = "";
                        		}
                        	} else {
                        		value = new DecimalFormat("0.##").format(cell.getNumericCellValue());
                        	}
                        	break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                        	try {
                            	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
	                        	if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                        		
	                        		Date date = cell.getDateCellValue();
	                        		if (date != null) {
	                        			value = new SimpleDateFormat("yyyy/M/d").format(date);
	                        		} else {
	                        			value = "";
	                        		}
	                        	} else {
	                        		value = new DecimalFormat("0。##").format(cell.getNumericCellValue());
	                        	}
                        	} catch(Exception e){
                        		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                            if (!("").equals(cell.getStringCellValue().trim())) {
	                            	value = cell.getStringCellValue().trim();
                            	} else {
                            		value = "";
                            	}
                        		logger.info(e.getMessage(),e);
                        	}
                            break;
                        case HSSFCell.CELL_TYPE_BLANK:
                            break;
                        case HSSFCell.CELL_TYPE_ERROR:
                            value = "";
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue() ? "Y":"N";
                            break;
                        default:
                            value = "";
                        }
                        rowBuffer.append(value.replaceAll("\n", " "));
                        rowBuffer.append(",");
                    } else {
                    	rowBuffer.append(",");
                    }
                }

                if(StringUtils.isEmpty(rowBuffer.toString().replaceAll(",",""))){
                	continue;
                }
                csvBuffer.append(rowBuffer.deleteCharAt(rowBuffer.lastIndexOf(",")));
                csvBuffer.append("\n");
            }
        }

        //write the string into the csv file
        File saveCSV = new File(csvPath);
        BufferedWriter writer = null;
        try {
            if(!saveCSV.exists())
                saveCSV.createNewFile();
            writer = new BufferedWriter(new FileWriter(saveCSV));
            writer.write(csvBuffer.toString());
            writer.close();
        } catch (Exception e) {
            throw new IOException(e);
        }finally {
			if (null != writer) {
				writer.close();
			}
		}
    }

    //获取工作簿的sheet
    private static Sheet getSheetByNum(Workbook book,int number){  
        Sheet sheet = null;
        try {
            sheet = book.getSheetAt(number);  
        } catch (Exception e) {
            throw new RuntimeException(e);  
        }
        return sheet;  
    }
    //获取当前Excel文件的工作簿
    private static Workbook getExcelWorkbook(String filePath) throws IOException{
        Workbook book = null;
        File file  = null;
        FileInputStream fis = null;

        try {
            file = new File(filePath);
            if(!file.exists()){
                throw new RuntimeException("文件不存在");  
            }else{
                fis = new FileInputStream(file);
                book = WorkbookFactory.create(fis); 
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(fis != null){
                fis.close();
            }
        }
        return book;
    }
}