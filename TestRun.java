package ExtractAutomation;

import java.io.IOException;
import java.util.Properties;

public class TestRun {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Properties properties = ConfigReader.loadproperties();
		// Example: Fetch database configuration properties dynamically
        String dbUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");
        String filePathConfig = properties.getProperty("field.postion.test");

        // Print the properties to verify they are loaded correctly
        System.out.println("Database URL: " + dbUrl);
        System.out.println("Database User: " + dbUser);
        System.out.println("Database Password: " + dbPassword);
        System.out.println("File Path: " + filePathConfig);

	}

}
