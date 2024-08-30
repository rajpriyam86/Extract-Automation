package ExtractAutomation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class FileReader {

	public static Map<String, ArrayList<String>> extractvalues() throws IOException {
		Properties properties = ConfigReader.loadproperties();
		String filepath = properties.getProperty("file.path");
		int StartRow = Integer.parseInt(properties.getProperty("start.position"));
		int EndRow = Integer.parseInt(properties.getProperty("end.position"));
		
		Map<String, ArrayList<String>> fileValue = new HashMap<>();
		FileInputStream fstream = new FileInputStream(filepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		int rowcount = 0;
		String linecount;

		// calling FieldDetails Method to get the excel sheet value
		ArrayList<Integer> position = FieldDetails.filedPosition();

		ArrayList<Integer> fieldToValidate = FieldToTest.fieldToTestList();

		while ((linecount = br.readLine()) != null) {
			if (rowcount >= StartRow && rowcount <= EndRow) {

				String MRN = linecount.substring(position.get(0) - 1, position.get(1)).trim();

				ArrayList<String> indexList = new ArrayList<>();
			
				if (properties.getProperty("field.TestAllField").equalsIgnoreCase("Yes")) {
					
					//To compare all the fields
					for (int i = 0; i < position.size(); i += 2) { // i += 2 , i = i + 2 ; both the statement is same

						indexList.add(linecount.substring(position.get(i) - 1, position.get(i + 1)).trim());
					}

				} else {

                    //To compare specific field
					for (int index : fieldToValidate) {
						indexList.add(linecount.substring(position.get(index * 2 - 2) - 1, position.get(index * 2 - 1)).trim());
						
					}
					
				}
				fileValue.put(MRN, indexList);
			}
			rowcount++;

		}
		br.close();
		return fileValue;

	}

}
