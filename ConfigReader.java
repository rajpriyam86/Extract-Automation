package ExtractAutomation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	static String filepath = "F:/Automation Project/Amazon.in/UdemyLearning/Config File/config.properties";
	public static Properties loadproperties () throws IOException {
		
		Properties properties =new Properties();
		FileInputStream configreader = new FileInputStream(filepath);
		properties.load(configreader);
		return properties;
		
	}

}
