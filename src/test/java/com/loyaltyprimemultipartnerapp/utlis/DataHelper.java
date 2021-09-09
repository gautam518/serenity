package com.loyaltyprimemultipartnerapp.utlis;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataHelper {
	public static HashMap<String,String> storeValues = new HashMap<String, String>();
	
	public static List<HashMap<String,String>> readExcelDatafromFile(String filePath, String sheetName){
	 
	//create Java List to store Hashmaps
	List <HashMap<String,String>>excelData = new ArrayList<>();
	 
	try{
		FileInputStream fs = new FileInputStream(filePath);
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet = wb.getSheet(sheetName);
		 
		//catch header row, so that you can use it as Key for your hashmap
		Row HeaderRow = sheet.getRow(0);
		 
		for(int r = 1; r<=sheet.getPhysicalNumberOfRows();r++){
		Row CurrentRow = sheet.getRow(r);
		//each row of data is stored in new hashmap
		HashMap<String,String> currentRowMap = new HashMap<String,String>();
		 
		for(int c=0;c<CurrentRow.getPhysicalNumberOfCells();c++){
			Cell CurrentCell = CurrentRow.getCell(c);
			System.out.print(CurrentCell.getStringCellValue() + "\t");
			currentRowMap.put(HeaderRow.getCell(c).getStringCellValue(),CurrentCell.getStringCellValue());
			// i.e hashmap<key, value> = <row(0)column(c), row(1)column(c)>
		}
		excelData.add(currentRowMap);
		}
		wb.close();
		fs.close();
	}
	catch(Exception e){
		e.printStackTrace();
	}
	 
	return excelData;
	 
	}
}