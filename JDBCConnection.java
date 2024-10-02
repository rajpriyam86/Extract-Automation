package ExtractAutomation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class JDBCConnection {
    private static HikariDataSource dataSource;

    static {
        // Configure HikariCP
        HikariConfig config = new HikariConfig();
        Properties properties;
		try {
			properties = ConfigReader.loadproperties();
		
        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.password"));
        config.setMaximumPoolSize(10); // Set the maximum number of connections in the pool
        dataSource = new HikariDataSource(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static ResultSet DatabaseConnection(String MRN) throws ClassNotFoundException, SQLException, IOException {
        // Get a connection from the pool
        try (Connection conn = dataSource.getConnection(); Statement s = conn.createStatement()) {
            ResultSet rs;

            if (ConfigReader.loadproperties().getProperty("field.TestAllField").equalsIgnoreCase("Yes")) {
                rs = s.executeQuery(ConfigReader.loadproperties().getProperty("sql.query.all") + "'" + MRN + "'");
            } else {
                rs = s.executeQuery(ConfigReader.loadproperties().getProperty("sql.query.specific") + "'" + MRN + "'");
            }

            return rs; // Note: You will need to handle closing the ResultSet outside this method.
        }
    }

    public static String getBillingStatusByMRN(String MRN) throws ClassNotFoundException, SQLException, IOException {
        // Get a connection from the pool
        try (Connection conn = dataSource.getConnection(); Statement s = conn.createStatement()) {
            ResultSet rs;

            // Modify query to return only the billing status
            rs = s.executeQuery("SELECT BILLING_STATUS FROM cam_customer_config ccc "
                    + "JOIN tap_person_coverage tpc ON tpc.group_id = ccc.group_id "
                    + "JOIN tap_coverage tc ON tc.coverage_id = tpc.coverage_id "
                    + "WHERE tpc.mrn_number = '" + MRN + "'");

            if (rs.next()) {
                return rs.getString("billing_status");
            }
            return "Non Migrated"; // Return "Non Migrated" if no result is found
        }
    }

    public static String getBillingStatusByGroup(String MRN) throws ClassNotFoundException, SQLException, IOException {
        // Get a connection from the pool
        try (Connection conn = dataSource.getConnection(); Statement s = conn.createStatement()) {
            ResultSet rs;

            // Modify query to return only the billing status
            rs = s.executeQuery("SELECT * FROM cam_customer_config WHERE group_id = '" + MRN + "'");

            if (rs.next()) {
                return rs.getString("billing_status");
            }
            return "Non Migrated"; // Return "Non Migrated" if no result is found
        }
    }
}
