package ExtractAutomation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

public class NewDBData {

    public static ArrayList<FieldsList> extractvalues(String MRN) throws ClassNotFoundException, SQLException, IOException {
        ArrayList<FieldsList> DBRowData = new ArrayList<>();

        // Load database connection details from properties file
        Properties properties = ConfigReader.loadproperties();
        String DBUrl = properties.getProperty("db.url");
        String Username = properties.getProperty("db.user");
        String DBpassword = properties.getProperty("db.password");

        // Establish database connection
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
        Statement s = conn.createStatement();
        ResultSet rs;

        // Execute the query using MRN
        rs = s.executeQuery(properties.getProperty("sql.query.all") + "'" + MRN + "'");

        // Fetch metadata for debugging or other purposes
        ResultSetMetaData rsmd = rs.getMetaData();
        if (!rs.isBeforeFirst()) {
            System.out.println("No data returned for MRN: " + MRN);
        }
        //test
     

        // Iterate over the result set rows
        while (rs.next()) {
        	DBRowData.clear();
            // Create a new FieldsList instance for each row
            FieldsList dbFields = new FieldsList();
            
            Object obj = rs.getObject(1);
            

           
            String MRN1 = rs.getString("MRN");            
            System.out.println("Fetched MRN: " + MRN1); // Debugging output
            dbFields.setMRN(String.valueOf(MRN1)); // Store MRN as string

            // Fetch the values from the current row
            dbFields.setMRN(MRN1);
            dbFields.setFirst_Name(rs.getString("FIRST_NAME"));
            dbFields.setMiddle_Name(rs.getString("MIDDLE_NAME"));
            dbFields.setLast_Name(rs.getString("LAST_NAME"));
            dbFields.setDOB(rs.getString("DOB"));
            dbFields.setGender(rs.getString("GENDER"));

            // Add the current row's FieldsList object to the list
            DBRowData.add(dbFields);
            
            
        }

        // Close the connection (optional, but good practice)
        conn.close();

        // Return the list of all rows
        return DBRowData;
    }
}
