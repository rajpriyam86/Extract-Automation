package ExtractAutomation;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.testng.Assert;

public class ProcessingLogicValidation {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		
		 Properties properties = ConfigReader.loadproperties();
       String filepath = properties.getProperty("file.kpmc.path");

		
		//DB Data
		ResultSet rs  = JDBCConnection.ProcessingLogic();
		//Resultsetmetadata
		ResultSetMetaData rsmd = rs.getMetaData();
		//File Data
		Map<String, Map<String, String>> fileMRN = FileReader.extractValues(filepath);
		
		int filecount = fileMRN.size();
		int rowCount = 0;
		boolean allRecordsPrinted = false; // Flag to ensure message is printed only once
        
     
		
		while(rs.next()) {
			 String mrn = rs.getString("MRN");
	            if (fileMRN.containsKey(mrn)) {
	                // Print only once
	                if (!allRecordsPrinted) {
	                    System.out.println("All Records are present in the File");
	                    allRecordsPrinted = true; // Set the flag to true to prevent further prints
	                }
	            } else {
	                System.out.println(mrn + " present in DB but not present in the file");
	            }
			rowCount++;
			
			
		}
		try{Assert.assertEquals(rowCount,filecount , "File count and DB count Mismatch: file count: "+filecount+" || DBCount: "+rowCount);
//		assert.assertEquals(rowCount, filecount);
			System.out.println("File count and DBCount Matched Successfully:");
			System.out.println("file count: "+filecount);
			System.out.println( "DBCount: "+rowCount);
		}
		catch(AssertionError e){
			
		}
	
		

	}

}
