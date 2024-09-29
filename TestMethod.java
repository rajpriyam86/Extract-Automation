package ExtractAutomation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TestMethod {

	public static void main(String[] args) throws Exception {
		Properties properties = ConfigReader.loadproperties();
		String KPMCfilepath = properties.getProperty("file.kpmc.path");
		// Define file path
		String outputfile = "F:/Automation Project/Amazon.in/UdemyLearning/src/ExtractAutomation/TestSummary.txt";
		int j = 1; // Creating this variable to print the MRN in output in sequence like 1), 2), 3)
					// this way

		// Initiating BufferedWriter to print the output in a file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile))) {
			writer.write("********** Test Summary **********");
			writer.newLine();
			writer.write("===================================");
			writer.newLine();
			writer.newLine();
			writer.write("###Data extracted from the file for validation from line number "+properties.getProperty("start.position")+ " To "+properties.getProperty("end.position") );
			writer.newLine();
			writer.write("===================================");
			writer.newLine();
			// Read file data
			Map<String, Map<String, String>> fileData = FileReader.extractValues(KPMCfilepath);
			// Read DefaultValues
			Map<String, String> defaultValues = FieldDetails.DefaultValueList();

			// Compare with DB data
			for (Map.Entry<String, Map<String, String>> entry : fileData.entrySet()) {
				String mrn = entry.getKey().split("-")[0];
				System.out.println(mrn);
				Map<String, String> fileFieldValues = entry.getValue();
				Map<String, String> dbFieldValues = DBData.extractValues(mrn);

				// Compare the maps for each MRN
				boolean hasIssues = false;

				writer.newLine();
				writer.write(j + ") MRN: " + mrn);
				writer.newLine();
				writer.write("----------------------------------");
				writer.newLine();

				// Compare field by field
				for (Map.Entry<String, String> fileEntry : fileFieldValues.entrySet()) {
					String fieldName = fileEntry.getKey();
					String fileValue = fileEntry.getValue();
					if (dbFieldValues.containsKey(fieldName)) {
						String dbValue = dbFieldValues.get(fieldName);

						if (!Objects.equals(fileValue, dbValue)) {
							if (!hasIssues) {
								writer.write("Issues found:");
								writer.newLine();
								hasIssues = true;
							}

							writer.write(" -Field Name: " + fieldName);
							writer.newLine();
							writer.write("    *File Data: " + fileValue);
							writer.newLine();
							writer.write("    *DB Data: " + dbValue);
							writer.newLine();
							writer.write("    *Expected Data: " + dbValue);
							writer.newLine();
							writer.newLine();
						}
					} else {
						String defaultvalue = defaultValues.get(fieldName);
						if (!Objects.equals(fileValue, defaultvalue)) {
							if (!hasIssues) {
								writer.write("Issues found:");
								writer.newLine();
								hasIssues = true;
							}

							writer.write(" -Field Name: " + fieldName);
							writer.newLine();
							writer.write("    *File Data: " + fileValue);
							writer.newLine();
							writer.write("    *Default Data: " + defaultvalue);
							writer.newLine();
							writer.write("    *Expected Data: " + defaultvalue);
							writer.newLine();
							writer.newLine();
						}

					}
				}

				if (!hasIssues) {
					writer.write("All fields validated successfully. No Issues found:");
					writer.newLine();
				}

				writer.write("===========================");
				writer.newLine(); // Add an extra line for readability
				j++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}