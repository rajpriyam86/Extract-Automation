package ExtractAutomation;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TestMethod {
	

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Properties properties = ConfigReader.loadproperties();
		// Define file path

		// Uncomment and modify according to your file reading method
		Map<String, ArrayList<String>> fileData = FileReader.extractvalues();

		ArrayList<String> filedName = FieldDetails.filedNameList("Field_Positions");
		
		ArrayList<Integer> fieldToValidate  = FieldToTest.fieldToTestList();

		for (Map.Entry<String, ArrayList<String>> entry : fileData.entrySet()) {
			String key = entry.getKey();
			ArrayList<String> list1 = entry.getValue();
			ArrayList<String> list2 = DBData.extractvalues(key);

			// Compare the lists for each key
			if (!Objects.equals(list1, list2)) {

				System.out.println("Difference found at MRN: " + key);

				// Find the maximum size to avoid IndexOutOfBoundsException
				int maxSize = Math.max(list1.size(), list2.size());

				// Compare field by field
				for (int i = 0; i < maxSize; i++) {
					String fileValue = i < list1.size() ? list1.get(i) : "null";   // Ternary Operator <statement> ? <true> : <false>
					String dbValue = i < list2.size() ? list2.get(i) : "null";
					
					if (!Objects.equals(fileValue, dbValue)) {
						
						if (properties.getProperty("field.TestAllField").equalsIgnoreCase("Yes")) {
							System.out.println("Field Name: " + filedName.get(i));
						}else {
							System.out.println("Field Name: " + filedName.get(fieldToValidate.get(i) - 1));							
						}						
						System.out.println("File Data: " + fileValue);
						System.out.println("DB Data: " + dbValue);
					}
				}
				System.out.println(); // Add an extra line for readability

			} else {
				System.out.println(key + " : Data are equal");
			}
		}

	}
}
