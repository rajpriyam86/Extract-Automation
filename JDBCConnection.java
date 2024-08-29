package ExtractAutomation; 

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCConnection {
	
	public static ResultSet DatabaseConnection (String MRN) throws ClassNotFoundException, SQLException, IOException
	{
		Properties properties = ConfigReader.loadproperties();
   	 String DBUrl = properties.getProperty("db.url");
   	 String Username = properties.getProperty("db.user");
   	 String DBpassword = properties.getProperty("db.password");
		List<String> DBField = new ArrayList<>();
		Class.forName("oracle.jdbc.driver.OracleDriver");  
		Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM TAP_PERSON WHERE MRN = '" + MRN + "'");
		

		
		
		
		return rs;
	}

}
