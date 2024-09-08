package ExtractAutomation;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DBData {

    public static Map<String, String> extractValues(String MRN) throws Exception {
        // Create a map to hold the row data, where the key is MRN_fieldName and value is the transformed field value
        Map<String, String> DBRowData = new HashMap<>();
        
        // Get ResultSet from database
        ResultSet values = JDBCConnection.DatabaseConnection(MRN);
        
        // Get ResultSetMetaData to retrieve column information
        ResultSetMetaData rsmd = values.getMetaData();

        // Date formatter for the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
        
        // Load the transformation logic from Excel file
        Map<String, String> fieldTransformations = FieldDetails.FieldsLogicList();

        // Iterate over rows of the ResultSet
        while (values.next()) {
            // Iterate over columns in the current row
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                Object obj = values.getObject(i);
                String fieldName = rsmd.getColumnName(i); // Get the field name from the ResultSet
                String fieldValue = "";

                if (obj instanceof Timestamp) {
                    // Convert Timestamp to java.sql.Date
                    Timestamp timestamp = (Timestamp) obj;
                    Date date = new Date(timestamp.getTime()); // Use java.sql.Date
                    // Format java.sql.Date to dd-MMM-yy
                    fieldValue = dateFormat.format(date);
                } else {
                    // Convert other types of objects to String
                    fieldValue = obj != null ? obj.toString() : "";
                }

                // Apply transformation to the field value using reflection
                String transformedValue = ApplyTransformation.applyTransformation(fieldName, fieldValue, fieldTransformations);

                // Combine MRN and field name to create a unique key
                String key = fieldName;

                // Put the key-value pair into the map
                DBRowData.put(key, transformedValue);
            }
        }

        // Return the map with MRN_fieldName as key and transformed field value as value
        return DBRowData;
    }
}