package ExtractAutomation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileReader {

    public static Map<String, Map<String, String>> extractValues() throws IOException {
        // Load properties file
        Properties properties = ConfigReader.loadproperties();
        String filepath = properties.getProperty("file.path");
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
        ArrayList<String> fieldNames = FieldDetails.filedNameList();  // Assuming field names can be fetched from FieldDetails
        
        // Get specific fields to validate
        ArrayList<Integer> fieldToValidate = FieldDetails.fieldToTestList();

        // Iterate over the file lines
        while ((linecount = br.readLine()) != null) {
            if (rowcount >= startRow && rowcount <= endRow) {
            	// Create a map to store field names and values for the current MRN
                Map<String, String> fieldMap = new HashMap<>();

                // Extract MRN from the file based on position
                String MRN = linecount.substring(position.get(0) - 1, position.get(1)).trim();

                // To handle all fields or specific fields based on configuration
                if (properties.getProperty("field.TestAllField").equalsIgnoreCase("Yes")) {
                    // Compare all fields
                    for (int i = 0; i < position.size(); i += 2) {
                        // Extract field value and corresponding field name
                        String fieldValue = linecount.substring(position.get(i) - 1, position.get(i + 1)).trim();
                        String fieldName = fieldNames.get(i / 2); // Get field name from fieldNames list

                        // Create key as MRN_fieldName
                        String key =  fieldName;
                        
                        // Store the key-value pair
                        fieldMap.put(key, fieldValue);
                    }

                } else {
                    // Compare only specific fields
                    for (int index : fieldToValidate) {
                        // Extract field value and corresponding field name for specific fields
                        String fieldValue = linecount.substring(position.get(index * 2 - 2) - 1, position.get(index * 2 - 1)).trim();
                        String fieldName = fieldNames.get(index - 1); // Get field name for the specific index
                        
                        // Create key as MRN_fieldName
                        String key = fieldName;

                        // Store the key-value pair
                        fieldMap.put(key, fieldValue);

//                        System.out.println("MRN: " + MRN + ", Field: " + fieldName + ", Value: " + fieldValue);
                    }
                }
                fileValue.put(MRN, fieldMap);
            }
            rowcount++;
        }

        br.close();
        return fileValue;
    }
}