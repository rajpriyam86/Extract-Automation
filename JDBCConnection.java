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
			rs = s.executeQuery(properties.getProperty("sql.query.all") + "'"+ MRN + "'");
		} else {
			rs = s.executeQuery( properties.getProperty("sql.query.specific") + "'" + MRN + "'");
		}

		return rs;
	}

}
