package ExtractAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FieldDetails {
	
	Properties properties;// = ConfigReader.loadproperties();
	String filePath, sheetname;
	// creating a clobel method to store the filepath & sheetname of the excel so that we can call this in the othyer methods
	public FieldDetails() throws IOException {
		properties = ConfigReader.loadproperties();
		filePath = properties.getProperty("field.details.filepath");
		sheetname = properties.getProperty("field.details.sheetname");
	}
	
	//storing the start and end positions of the feilds from Excel in an arraylist
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
	
	// Storing the field names from the excel file in an Arraylist
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
	
	// storing the Fielname values from excel file and storing it in a Map where key is the field name and value is it's index number
	
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
	
	//Extracting the Transformation Logic methods name as String  from the Excel File
	
	public static Map<String, String> FieldsLogicList() throws IOException {
		FieldDetails myObj = new FieldDetails();

		Properties properties = ConfigReader.loadproperties();
		Map<String, String> fieldTransformations = new HashMap<>();
		FileInputStream file = new FileInputStream(properties.getProperty("field.details.filepath"));
		Workbook workbook = new XSSFWorkbook(file);

		Sheet sheet = workbook.getSheet(myObj.sheetname);
		
	

		for (int rowindex = 1; rowindex <= sheet.getLastRowNum(); rowindex++) {
			Row row = sheet.getRow(rowindex);
			if (row == null) {
				continue; // Skip the empty row
			}

			Cell fieldNameCell = row.getCell(0);
			Cell transformation = row.getCell(3);
			String fieldNames = fieldNameCell.getStringCellValue();
			String logic = (transformation == null || transformation.getCellType() == CellType.BLANK) ? 
	                "null" : transformation.getStringCellValue().trim();
			
			;

			fieldTransformations.put(fieldNames.toUpperCase(), logic);

		}
		workbook.close(); 
		return fieldTransformations;

	}
	
	
	//Extracting the Fields to test mentioned in the configuration file and storing it in a array
	public static ArrayList<Integer> fieldToTestList() throws IOException {
		Properties properties = ConfigReader.loadproperties();

		String str2 = properties.getProperty("field.position.test.string");

		// Step 1: Split the string by comma to get a String array
		String[] stringArray2 = str2.split(",");		
		
		Map<String, Integer> fieldPostionList = FieldDetails.fieldNameListMap();
				
		// Step 2: Create an int array of the same length
		ArrayList<Integer> intArray = new ArrayList<>();

		// Step 3: Parse each element of the String array to an int and store it in the
		// int array
		for (int i = 0; i < stringArray2.length; i++) {
			
			if (fieldPostionList.containsKey(stringArray2[i].toUpperCase().trim())) {
				intArray.add(fieldPostionList.get(stringArray2[i].toUpperCase().trim())); // Convert to int and store
			} else {
				System.out.println("Field Details Missing : " + stringArray2[i].toUpperCase());
				System.out.println("Kindly validate the fields and try again.");
				System.exit(0);
			}
		}
		return intArray;

	}

}
