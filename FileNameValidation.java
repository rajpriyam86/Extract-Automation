package ExtractAutomation;

import java.util.Properties;

import org.testng.Assert;

public class FileNameValidation {
	public static void main(String[] args) throws Exception {
		Properties properties = ConfigReader.loadproperties();
		
		String FileNameFromLZ = WinSCPJava.fetchAndReadLatestFile();
		String ActualFileName = properties.getProperty("kpmc.file.name");
		  Assert.assertTrue(FileNameFromLZ.contains(ActualFileName), 
			        "The file name '" + FileNameFromLZ + "' does not contain the expected substring '" + ActualFileName + "'");
		
	
}

}
