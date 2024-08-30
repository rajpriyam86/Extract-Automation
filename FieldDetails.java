package ExtractAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FieldDetails {
	
	Properties properties;// = ConfigReader.loadproperties();
	String filePath, sheetname;
	
	public FieldDetails() throws IOException {
		properties = ConfigReader.loadproperties();
		filePath = properties.getProperty("field.details.filepath");
		sheetname = properties.getProperty("field.details.sheetname");
	}
	
	
	public static ArrayList<Integer> filedPosition() throws IOException {
		
		FieldDetails myObj = new FieldDetails();
		
		ArrayList<Integer> position = new ArrayList<>();
		FileInputStream fileInputStream = new FileInputStream(new File(myObj.filePath));
		Workbook workbook = new XSSFWorkbook(fileInputStream);
		Sheet sheet = workbook.getSheet(myObj.sheetname);

		for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				// Handle empty rows as needed
				continue;
			}
			// Iterate through each cell in the row

			for (int cellIndex = 1; cellIndex < row.getLastCellNum(); cellIndex++) {

				Cell cell = row.getCell(cellIndex);

				if (cell == null) {
					// Handle empty cells as needed
					continue;
				}
				switch (cell.getCellType()) {
				case STRING:
//                     System.out.print(cell.getStringCellValue() + "\t");
					break;
				case NUMERIC:
					position.add((int) cell.getNumericCellValue());
//                     System.out.print((int)cell.getNumericCellValue() + "\t");
					break;
				default:
//					System.out.print("UNKNOWN TYPE" + "\t");
					break;
				}

			}
		}
		workbook.close();
		return position;

	}
	
	
	public static ArrayList<String> filedNameList() throws IOException{
		FieldDetails myObj = new FieldDetails();
		
		ArrayList<String> fieldname = new ArrayList<>();
		FileInputStream fileInputStream = new FileInputStream(new File(myObj.filePath));
		Workbook workbook = new XSSFWorkbook(fileInputStream);
		Sheet sheet = workbook.getSheet(myObj.sheetname);
		
		for(int rowindex=1;rowindex <= sheet.getLastRowNum();rowindex++) {
			Row row = sheet.getRow(rowindex);
			
			Cell cell = row.getCell(0);
			
			fieldname.add(cell.getStringCellValue().toUpperCase());
		}
		
		workbook.close();
		return fieldname;
		
	}
	
	public static Map<String, Integer> fieldNameListMap() throws IOException{
		
		FieldDetails myObj = new FieldDetails();
		
		FileInputStream fileInputStream = new FileInputStream(new File(myObj.filePath));
		Workbook workbook = new XSSFWorkbook(fileInputStream);
		Sheet sheet = workbook.getSheet(myObj.sheetname);
		Map<String, Integer> fieldName = new HashMap<String, Integer>();
		
		for(int rowIndex=1;rowIndex <= sheet.getLastRowNum();rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			
			Cell cell = row.getCell(0);
			
			fieldName.put(cell.getStringCellValue().toUpperCase(), rowIndex);
		}
		
		
		workbook.close();
		return fieldName;
	}

}
