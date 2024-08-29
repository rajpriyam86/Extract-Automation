package ExtractAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FieldDetails {
	static String filePath = "F:/Automation Project/Amazon.in/UdemyLearning/Config File/Field_Details.xlsx";

	public static ArrayList<Integer> filedPosition(String sheetname) throws IOException {
		
		Properties properties = ConfigReader.loadproperties();
	   	 String filePath = properties.getProperty("field.details.filepath");
		
		ArrayList<Integer> position = new ArrayList<>();
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		Workbook workbook = new XSSFWorkbook(fileInputStream);
		Sheet sheet = workbook.getSheet(sheetname);

		for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				// Handle empty rows as needed
//				System.out.println("EMPTY ROW");
				continue;
			}
//			  System.out.println(row.getLastCellNum());
			// Iterate through each cell in the row

			for (int cellIndex = 1; cellIndex < row.getLastCellNum(); cellIndex++) {

				Cell cell = row.getCell(cellIndex);

				if (cell == null) {
					// Handle empty cells as needed
//					System.out.print("EMPTY CELL" + "\t");
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
//			System.out.println();
		}

		return position;

	}
	
	
	public static ArrayList<String> filedNameList(String sheetname) throws IOException{
		
		ArrayList<String> fieldname = new ArrayList<>();
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		Workbook workbook = new XSSFWorkbook(fileInputStream);
		Sheet sheet = workbook.getSheet(sheetname);
		
		for(int rowindex=1;rowindex <=sheet.getLastRowNum();rowindex++) {
			Row row = sheet.getRow(rowindex);
			
			Cell cell = row.getCell(0);
			
			fieldname.add(cell.getStringCellValue());
//			System.out.println(cell);
		}
		
		return fieldname;
		
	}

}
