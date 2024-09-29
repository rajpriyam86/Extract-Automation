package ExtractAutomation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCConnection {

	public static ResultSet DatabaseConnection(String MRN) throws ClassNotFoundException, SQLException, IOException {
		Properties properties = ConfigReader.loadproperties();
		String DBUrl = properties.getProperty("db.url");
		String Username = properties.getProperty("db.user");
		String DBpassword = properties.getProperty("db.password");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
		Statement s = conn.createStatement();
		ResultSet rs;

		if (properties.getProperty("field.TestAllField").equalsIgnoreCase("Yes")) {
			rs = s.executeQuery(properties.getProperty("sql.query.all") + "'" + MRN + "'");

		} else {
			rs = s.executeQuery(properties.getProperty("sql.query.specific") + "'" + MRN + "'");

		}

		return rs;
	}
	
	
	// Migration status check (imported from ChatGPT)
	
	public static String getBillingStatusByMRN(String MRN) throws ClassNotFoundException, SQLException, IOException {
	    Properties properties = ConfigReader.loadproperties();
	    String DBUrl = properties.getProperty("db.url");
	    String Username = properties.getProperty("db.user");
	    String DBpassword = properties.getProperty("db.password");
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
	    Statement s = conn.createStatement();
	    ResultSet rs;
	    
	    // Modify query to return only the billing status
	    rs = s.executeQuery("SELECT billing_status FROM cam_customer_config ccc " +
	            "JOIN tap_person_coverage tpc ON tpc.group_id = ccc.group_id and ccc.COVERAGE_STATUS = 'Covered'" +
	            "WHERE tpc.mrn_number = '" + MRN + "'");

	    if (rs.next()) {
	        return rs.getString("billing_status");
	    }
	    return "Non Migrated"; // Return "Non Migrated" if no result is found
	}

	
	public static String getBillingStatusByGroup(String MRN) throws ClassNotFoundException, SQLException, IOException {
	    Properties properties = ConfigReader.loadproperties();
	    String DBUrl = properties.getProperty("db.url");
	    String Username = properties.getProperty("db.user");
	    String DBpassword = properties.getProperty("db.password");
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
	    Statement s = conn.createStatement();
	    ResultSet rs;
	    
	    // Modify query to return only the billing status
	    rs = s.executeQuery("select * from cam_customer_config where group_id =  '" + MRN + "'");

	    if (rs.next()) {
	        return rs.getString("billing_status");
	    }
	    return "Non Migrated"; // Return "Non Migrated" if no result is found
	}
	

}
