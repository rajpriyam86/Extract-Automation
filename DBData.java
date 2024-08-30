package ExtractAutomation;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DBData {

    public static ArrayList<String> extractvalues(String MRN) throws ClassNotFoundException, SQLException, IOException {
        // Create a list to hold the row data
        ArrayList<String> DBRowData = new ArrayList<>();
        
        // Get ResultSet from database
        ResultSet values = JDBCConnection.DatabaseConnection(MRN);
        
        // Get ResultSetMetaData to retrieve column information
        ResultSetMetaData rsmd = values.getMetaData();

        // Date formatter for the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

        // Iterate over rows of the ResultSet
        while (values.next()) {
            // Clear previous row data
            DBRowData.clear();

            // Iterate over columns in the current row
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                Object obj = values.getObject(i);

                if (obj instanceof Timestamp) {
                    // Convert Timestamp to java.sql.Date
                    Timestamp timestamp = (Timestamp) obj;
                    Date date = new Date(timestamp.getTime()); // Use java.sql.Date
                    // Format java.sql.Date to dd-MMM-yy
                    String formattedDate = dateFormat.format(date);
                    DBRowData.add(formattedDate);
                } else {
                    // Convert other types of objects to String
                    DBRowData.add(obj != null ? obj.toString() : "");
                }
            }

            // Add the current row's data to the result list
            // This assumes only one row of data is needed; adjust if multiple rows should be collected
        }
        
        return DBRowData;
    }
}
