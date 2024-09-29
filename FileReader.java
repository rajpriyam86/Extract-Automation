package ExtractAutomation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class FileReader {

//	Reading the normal files and printing them in the Map<String, Map<String, String>>
	public static Map<String, Map<String, String>> extractValues(String filepath) throws IOException {
		// Load properties file
		Properties properties = ConfigReader.loadproperties();
//		String filepath = properties.getProperty("file.path"); //change to generalise the input file instead of hardcode it
		int startRow = Integer.parseInt(properties.getProperty("start.position"));
		int endRow = Integer.parseInt(properties.getProperty("end.position"));

		// Map to store MRN_fieldName as key and fieldValue as value
		Map<String, Map<String, String>> fileValue = new HashMap<>();

		FileInputStream fstream = new FileInputStream(filepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		int rowcount = 0;
		String linecount;

		// Get field positions and names from FieldDetails
		ArrayList<Integer> position = FieldDetails.filedPosition();
		ArrayList<String> fieldNames = FieldDetails.filedNameList(); // Assuming field names can be fetched from
																		// FieldDetails

		// Get specific fields to validate
		ArrayList<Integer> fieldToValidate = FieldDetails.fieldToTestList();

		// Iterate over the file lines
		while ((linecount = br.readLine()) != null) {
//			if (rowcount >= startRow && rowcount <= endRow) {
			// Create a map to store field names and values for the current MRN
			Map<String, String> fieldMap = new LinkedHashMap<>();

			// Extract MRN from the file based on position
			String MRN = linecount.substring(position.get(0) - 1, position.get(1)).trim();
			String Name = linecount.substring(position.get(2) - 1, position.get(3)).trim();
			String Group = linecount.substring(position.get(34) - 1, position.get(35)).trim();

//			System.out.println(MRN);

			// To handle all fields or specific fields based on configuration
			if (properties.getProperty("field.TestAllField").equalsIgnoreCase("Yes")) {
				// Compare all fields
				for (int i = 0; i < position.size(); i += 2) {
					// Extract field value and corresponding field name
					String fieldValue = linecount.substring(position.get(i) - 1, position.get(i + 1)).trim();
					String fieldName = fieldNames.get(i / 2); // Get field name from fieldNames list

					// Create key as MRN_fieldName
					String key = fieldName;

					// Store the key-value pair
					fieldMap.put(key, fieldValue);
				}

			} else {
				// Compare only specific fields
				for (int index : fieldToValidate) {
					// Extract field value and corresponding field name for specific fields
					String fieldValue = linecount
							.substring(position.get(index * 2 - 2) - 1, position.get(index * 2 - 1)).trim();
					String fieldName = fieldNames.get(index - 1); // Get field name for the specific index

					// Create key as MRN_fieldName
					String key = fieldName;

					// Store the key-value pair
					fieldMap.put(key, fieldValue);

//                        System.out.println("MRN: " + MRN + ", Field: " + fieldName + ", Value: " + fieldValue);
				}
			}

			fileValue.put(MRN + "-"+ Name + "-" + Group, fieldMap);

		}
		rowcount++;
//		}

		br.close();
		return fileValue;
	}

//   Reading Out file
	public static List<List<String>> readOutFile() throws IOException {
		// Load properties file
		Properties properties = ConfigReader.loadproperties();
		String filepath = properties.getProperty("file.out.path");

		// 2D array to store the out file data
		List<List<String>> outData = new ArrayList<>();

		FileInputStream fstream = new FileInputStream(filepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		int row = 0;
		String linecount;

		// Get field positions and names from FieldDetails
		ArrayList<Integer> position = FieldDetails.filedPosition();

		// Iterate over the file lines
		while ((linecount = br.readLine()) != null) {

			outData.add(new ArrayList<>()); // Create new row
			for (int i = 0; i < position.size(); i += 2) {
				// Extract field value and corresponding field name
				String fieldValue = linecount.substring(position.get(i) - 1, position.get(i + 1)).trim();
				outData.get(row).add(fieldValue);
			}
			row++;
		}

		br.close();
//		for(List<String> data:outData) {
//			System.out.println(data);			
//		}
		return outData;
	}

	// Method to read OUT file and store sorted data (new Method From ChatGPT)
	public static List<List<String>> readAndSortOutFile() {
		List<List<String>> outData = null;
		try {
			outData = readOutFile();
			// Sort the OUT file data based on MRN and other fields
			
			Collections.sort(outData, (record1, record2) -> {
			    // Step 1: Compare by MRN (index 0)
			    int comparison = record1.get(0).compareTo(record2.get(0));
			    if (comparison != 0) {
			        return comparison;
			    }
			    
			    // Step 2: Compare by Name (index 1)
			    comparison = record1.get(1).compareTo(record2.get(1));
			    if (comparison != 0) {
			        return comparison;
			    }
			    
			    // Step 3: Compare by Group ID (index 17)
			    comparison = record1.get(17).compareTo(record2.get(17));
			    if (comparison != 0) {
			        return comparison;
			    }
			    
			    // Step 4: Compare the rest of the fields except MRN (0), Name (1), and Group ID (17)
			    for (int i = 2; i < record1.size(); i++) {
			        // Skip comparing Group ID (index 17) again
			        if (i == 17) {
			            continue;
			        }
			        
			        comparison = record1.get(i).compareTo(record2.get(i));
			        if (comparison != 0) {
			            return comparison;  // Return as soon as a difference is found
			        }
			    }
			    
			    // If all fields are equal, return 0 (equal records)			    			    
			    System.out.println("Same record found: \n" + record1);
			    return 0;
			});


		} catch (Exception e) {
			System.out.println("error in the readAndSortOutFile method in filereader");
			System.out.println(e);
		}

		return outData;

	}

}