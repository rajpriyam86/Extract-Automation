package ExtractAutomation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TestMethod {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Properties properties = ConfigReader.loadproperties();
		// Define file path
		String outputfile = "F:/Automation Project/Amazon.in/UdemyLearning/src/ExtractAutomation/TestSummary.txt";
		int j =1; // creating this variable to print the mrn in output in sequence like 1),2),3) this way 

		// initiating BufferedWriter to print the output in a file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile))) {
			writer.write("********** Test Summary **********");
			writer.newLine();
			writer.write("===================================");
			writer.newLine();

			// Uncomment and modify according to your file reading method
			Map<String, ArrayList<String>> fileData = FileReader.extractvalues();

			ArrayList<String> filedName = FieldDetails.filedNameList();

			ArrayList<Integer> fieldToValidate = FieldToTest.fieldToTestList();

			for (Map.Entry<String, ArrayList<String>> entry : fileData.entrySet()) {
				String key = entry.getKey();
				ArrayList<String> list1 = entry.getValue();
				ArrayList<String> list2 = DBData.extractvalues(key);

				// Compare the lists for each key
				if (!Objects.equals(list1, list2)) {
					writer.newLine();
					writer.write(j+") MRN: " + key);
					writer.newLine();
					writer.write("----------------------------------");
					writer.newLine();
					writer.write("Issues found:");
					writer.newLine();

					// Find the maximum size to avoid IndexOutOfBoundsException
					int maxSize = Math.max(list1.size(), list2.size());

					// Compare field by field
					for (int i = 0; i < maxSize; i++) {
						String fileValue = i < list1.size() ? list1.get(i) : "null"; // Ternary Operator <statement> ?
																						// <true> : <false>
						String dbValue = i < list2.size() ? list2.get(i) : "null";

						if (!Objects.equals(fileValue, dbValue)) {

							if (properties.getProperty("field.TestAllField").equalsIgnoreCase("Yes")) {
								writer.write(" -Field Name: " + filedName.get(i));
							} else {
								writer.write(" -Field Name: " + filedName.get(fieldToValidate.get(i) - 1));
							}
							writer.newLine();
							writer.write("    *File Data: " + fileValue);
							writer.newLine();
							writer.write("    *DB Data: " + dbValue);
							writer.newLine();
							writer.newLine();

						}
					}
					writer.write("===========================");
					writer.newLine(); // Add an extra line for readability

				} else {
					writer.newLine();
					writer.write(j+") MRN: " + key);
					writer.newLine();
					writer.newLine();
					writer.write("All fields validated successfully No Issues found:");
					writer.newLine();
					writer.newLine();
					writer.write("=======================================================");
					writer.newLine();
				}
				j++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
