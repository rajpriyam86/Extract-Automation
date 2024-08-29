package ExtractAutomation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import SelfAutomation.PersonRecord;

public class FileReader {

	public static Map<String, ArrayList<String>> extractvalues( int StartRow, int EndRow) throws IOException {
		Properties properties = ConfigReader.loadproperties();
   	 String filepath = properties.getProperty("file.path");
		Map<String, ArrayList<String>> fileValue = new HashMap();
		FileInputStream fstream = new FileInputStream(filepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		int rowcount = 0;
		String linecount;
		
		//calling FieldDetails Method to get the excel sheet value
		ArrayList<Integer> position = FieldDetails.filedPosition("Field_Positions"); 
		
		while ((linecount = br.readLine()) != null) {
			if (rowcount > StartRow && rowcount < EndRow) {

				String MRN = linecount.substring(position.get(0) - 1, position.get(1)).trim();

				ArrayList<String> indexList = new ArrayList();
				for (int i = 0; i < position.size(); i += 2) { // i += 2 , i = i + 2 ; both the statement is same
					
					indexList.add(linecount.substring(position.get(i) - 1, position.get(i + 1)).trim());
				}

				fileValue.put(MRN, indexList);
			}
			rowcount++;

		}

		return fileValue;

	}

}
