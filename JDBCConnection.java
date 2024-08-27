package ExtractAutomation; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCConnection {
	
	public static ResultSet DatabaseConnection (String MRN) throws ClassNotFoundException, SQLException
	{
		List<String> DBField = new ArrayList<>();
		Class.forName("oracle.jdbc.driver.OracleDriver");  
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "priyam", "12345");
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM TAP_PERSON WHERE MRN = '" + MRN + "'");
		

		
		
		
		return rs;
	}

}
