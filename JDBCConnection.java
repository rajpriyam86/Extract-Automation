package ExtractAutomation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCConnection {
	public static Properties properties;
	static {
		try {
			properties = ConfigReader.loadproperties();
		} catch (IOException e) {
			e.printStackTrace();
			// You can also add some fallback logic here if needed
		}
	}
	public static String DBUrl = properties.getProperty("db.url");
	public static String Username = properties.getProperty("db.user");
	public static String DBpassword = properties.getProperty("db.password");

	public static ResultSet DatabaseConnection(String MRN) throws ClassNotFoundException, SQLException, IOException {
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

	public static ResultSet JobRunStatus(String job_id) throws ClassNotFoundException, SQLException, IOException {
		Class.forName("oracle.jdbc.driver.OracleDriver");

		Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
		Statement s = conn.createStatement();
		ResultSet rs;
		
		 String query = (properties.getProperty("sql.query.Job.status")+ "'" + job_id + "'");
		rs = s.executeQuery(query);
		

		return rs;
	}
	
	public static ResultSet ProcessingLogic() throws ClassNotFoundException, SQLException, IOException {
		Class.forName("oracle.jdbc.driver.OracleDriver");

		Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
		Statement s = conn.createStatement();
		ResultSet rs;
		
		 String query = (properties.getProperty("sql.query.processing.logic"));
		rs = s.executeQuery(query);
		

		return rs;
	}

}
