package ExtractAutomation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FilterMergeValidation {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Properties properties = ConfigReader.loadproperties();
	       String kpmcFilePath = properties.getProperty("file.kpmc.path");
	       String legacyFilePath = properties.getProperty("ile.legacy.path");
	       String outFilePath = properties.getProperty("ile.legacy.path");
	       
	       //Map to store the value 
	       Map<String,String> kpmcFileValue = new HashMap();
	       //loading KPMC File Data
	       Map<String, Map<String, String>> kpmcfileData = FileReader.extractValues(kpmcFilePath);
	       //Loading Legacy File Data
	       Map<String, Map<String, String>> legacyfileData = FileReader.extractValues(legacyFilePath);
	     //Loading OUT File Data
	       Map<String, Map<String, String>> outfileData = FileReader.extractValues(outFilePath);
	       
	       //Storing the group field
	       for (Map.Entry<String, Map<String, String>> entry : kpmcfileData.entrySet()) {
	    	   
	    	   
//	    	   kpmcFileValue.put(entry.getKey(), entry.getValue());
	       }
	       
	      

	}

}
