package ExtractAutomation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    // Method to load properties from multiple hard-coded files
    public static Properties loadproperties() throws IOException {
        Properties properties = new Properties();
        
        // Hard-coded file paths
        String[] filepaths = {
            "F:/Automation Project/Amazon.in/UdemyLearning/Config File/config.properties",
            "F:/Automation Project/Amazon.in/UdemyLearning/Config File/test.properties"
        };
        
        for (String filepath : filepaths) {
            try (FileInputStream configReader = new FileInputStream(filepath)) {
                properties.load(configReader);
            }
        }
        
        return properties;
    }
}
