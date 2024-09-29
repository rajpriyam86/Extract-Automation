package ExtractAutomation.File_comparision;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ExtractAutomation.ConfigReader;
import ExtractAutomation.FieldDetails;
import ExtractAutomation.FileReader;
import ExtractAutomation.JDBCConnection;
import ExtractAutomation.TransformationMethods;

public class Comparision {
	public static void main(String[] args) throws IOException {

		// Storing the data from all three files
		Properties properties = ConfigReader.loadproperties();
		String KPMCfilepath = properties.getProperty("file.kpmc.path");
		String LEGACYfilepath = properties.getProperty("file.legacy.path");
		String OUTfilepath = properties.getProperty("file.out.path");

		Map<String, Map<String, String>> kpmcData = FileReader.extractValues(KPMCfilepath);

		Map<String, Map<String, String>> legacyData = FileReader.extractValues(LEGACYfilepath);

		List<List<String>> sortedOutData = FileReader.readAndSortOutFile();
		try {
			validateFileskpmcData(kpmcData, sortedOutData);
			System.out.println("\n**********************************************************\n");
			validateFileslegacyData(legacyData, sortedOutData);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error found in validateFiles method");
			System.out.println("Error:\n" + e);
		}

//	    int dataFoundInOutFile = BinarySearch.binarySearch(sortedOutData, "452135-Arpan-1004");

//	    if(dataFoundInOutFile == -1) {
//	    	System.out.println("Record not found");
//	    }else {
//	    	System.out.println("Record found at index: " + dataFoundInOutFile);
//	    	
//	    }

//	    System.out.println("this is the out file sorted data");
//	    int j = 0;
//	    for(List<String> data : sortedOutData) {
//	    	System.out.println(j + " : " + data);
//	    	j++;
//	    	
//	    }

//	    String filepath = properties.getProperty("file.out.path");
//
//		FileInputStream fstream = new FileInputStream(filepath);
//		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//
//		int row = 0;
//		String linecount;
//		List<String> temp = null;
//
//		// Get field positions and names from FieldDetails
//		ArrayList<Integer> position = FieldDetails.filedPosition();
//
//		// Iterate over the file lines
//		while ((linecount = br.readLine()) != null) {
//
//			for (int i = 0; i < position.size(); i += 2) {
//				// Extract field value and corresponding field name
//				String fieldValue = linecount.substring(position.get(i) - 1, position.get(i + 1)).trim();
//				temp.add(fieldValue)
//			}
//			row++;
//		}
//
//		br.close();	    

	}

	// Main validation method for KPMC and Legacy(imported from ChatGPT)
	public static void validateFileskpmcData(Map<String, Map<String, String>> kpmcData, List<List<String>> sortedOutData) throws SQLException, IOException, ClassNotFoundException {

	    int kpmcMigratedCount = 0;
	    int legacyNonMigratedCount = 0;
	    int outFileTotalCount = sortedOutData.size();
	    int validatedCount = 0;

	    // Print all OUT File Records for debugging
//	    System.out.println("OUT File Records:");
//	    for (List<String> record : sortedOutData) {
//	        System.out.println(record);
//	    }
	    
	    System.out.println("\nKPMC Data Start\n");
	    
	    // Iterate over KPMC data
	    for (String compositeKey : kpmcData.keySet()) {
	        String mrn = compositeKey.split("-")[0];
	        
	        // Check migration status from the database
	        String billingStatus = JDBCConnection.getBillingStatusByMRN(mrn);  // migrated or (non-migrated,etc anything else)
	        System.out.println("Billing Status: " + billingStatus);
	        if ("Migrated".equalsIgnoreCase(billingStatus)) {
	            kpmcMigratedCount++;
	            List<String> kpmcRecord = new ArrayList<>(kpmcData.get(compositeKey).values());
//	            System.out.println("KPMC Record: " + kpmcRecord);
	            
	            // Search in OUT file
	            boolean isFound = BinarySearch.binarySearch(kpmcRecord, sortedOutData);
	            System.out.println("Migrated Search Result: " + isFound);
	            System.out.println(kpmcRecord);
	            if (!isFound) {
	                validatedCount++;
	                System.out.println("Error: MRN " + mrn + " Present in KPMC File and also is migrated but not found in OUT file.");
	            }
	        } else {
	            List<String> kpmcRecord = new ArrayList<>(kpmcData.get(compositeKey).values());
	          
	            boolean isFound = BinarySearch.binarySearch(kpmcRecord, sortedOutData);
	            System.out.println("non migrated Search Result: " + isFound);
	            System.out.println(kpmcRecord);
	            if (isFound) {
	                System.out.println("Error: MRN " + mrn + " Present in KPMC File and also is not migrated but found in OUT file.");
	            }
	        }
	        System.out.println();
	    }
	    System.out.println("KPMC Data End");
	}

	public static void validateFileslegacyData(Map<String, Map<String, String>> legacyData, List<List<String>> sortedOutData) throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nLegacy Data Start\n");
		// Iterate over Legacy data
		for (String compositeKey : legacyData.keySet()) {
			String mrn = compositeKey.split("-")[0];
			String group = compositeKey.split("-")[2];

			
			// Check migration status from the database
			String billingStatus = JDBCConnection.getBillingStatusByGroup(group);
			System.out.println("Billing Status: " + billingStatus);

			List<String> legacyRecord = new ArrayList<>(legacyData.get(compositeKey).values());

			// Search in OUT file
			boolean isFound = BinarySearch.binarySearch(legacyRecord, sortedOutData);
			System.out.println("Search Result: " + isFound);
			System.out.println(legacyRecord);

			if ("Migrated".equalsIgnoreCase(billingStatus)) {
				if (isFound) {
					System.out.println("Error: MRN " + mrn
							+ " Present in LEGACY File and also is migrated but found in OUT file.");
				}
			} else {
				if (!isFound) {
					System.out.println("Error: MRN " + mrn
							+ " Present in Legacy File and also is non-migrated but not found in OUT file.");
				}
			}
			System.out.println();
		}
		System.out.println("Legacy Data End");

//	    // Print counts and validation summary
//	    System.out.println("KPMC Migrated Count: " + kpmcMigratedCount);
//	    System.out.println("Legacy Non Migrated Count: " + legacyNonMigratedCount);
//	    System.out.println("OUT File Total Record Count: " + outFileTotalCount);
//	    System.out.println("Total Records Validated: " + validatedCount);
//
//	    // Check if counts match the expected result
//	    if (kpmcMigratedCount + legacyNonMigratedCount == outFileTotalCount) {
//	        System.out.println("The total record counts are consistent.");
//	    } else {
//	        System.out.println("Warning: The total record counts do not match.");
//	    }
	}

}
