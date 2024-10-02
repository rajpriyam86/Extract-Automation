package ExtractAutomation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TestMethod {

	public static void main(String[] args) throws Exception {
		Properties properties = ConfigReader.loadproperties();
		String KPMCfilepath = properties.getProperty("file.kpmc.path");
		String outputfile = "F:/Automation Project/Amazon.in/UdemyLearning/src/ExtractAutomation/ValidationAgainstDB.txt";

		// Counters for MRN tracking
		int totalValidated = 0;
		int totalWithIssues = 0;

		// Start writing to file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile))) {
			writer.write("********** Test Summary **********");
			writer.newLine();
			writer.write("===================================");
			writer.newLine();
			writer.newLine();
			writer.write("### Data extracted from the file for validation from line number "
					+ properties.getProperty("start.position") + " To " + properties.getProperty("end.position"));
			writer.newLine();
			writer.write("===================================");
			writer.newLine();

			// Read file data
			Map<String, Map<String, String>> fileData = FileReader.extractValues(KPMCfilepath);
			// Read default values
			Map<String, String> defaultValues = FieldDetails.DefaultValueList();

			// Compare with DB data
			for (Map.Entry<String, Map<String, String>> entry : fileData.entrySet()) {
				String mrn = entry.getKey().split("-")[0]; // Get MRN
				Map<String, String> fileFieldValues = entry.getValue();
				Map<String, String> dbFieldValues = DBData.extractValues(mrn);

				totalValidated++; // Increment total MRNs validated
				boolean hasIssues = false; // Track if the MRN has issues

				// If MRN does not exist in the DB, log it as an issue
				if (dbFieldValues == null || dbFieldValues.isEmpty()) {
					hasIssues = true;
					totalWithIssues++; // Increment issues count
					writer.newLine();
					writer.write("MRN: " + mrn + " - This MRN doesn't exist in the database.");
					writer.newLine();
				} else {
					// Compare field by field
					for (Map.Entry<String, String> fileEntry : fileFieldValues.entrySet()) {
						String fieldName = fileEntry.getKey();
						String fileValue = fileEntry.getValue();

						if (dbFieldValues.containsKey(fieldName)) {
							String dbValue = dbFieldValues.get(fieldName);

							// Check if file value and DB value don't match
							if (!Objects.equals(fileValue, dbValue)) {
								if (!hasIssues) {
									writer.newLine();
									writer.write("MRN: " + mrn);
									writer.newLine();
									writer.write("Issues found:");
									hasIssues = true;
									totalWithIssues++; // Increment issues count
								}

								writer.write(" - Field Name: " + fieldName);
								writer.newLine();
								writer.write("    * File Data: " + fileValue);
								writer.newLine();
								writer.write("    * DB Data: " + dbValue);
								writer.newLine();
								writer.write("    * Expected Data: " + dbValue);
								writer.newLine();
							}
						} else {
							// Field not present in DB, use default value for comparison
							String defaultvalue = defaultValues.get(fieldName);
							if (!Objects.equals(fileValue, defaultvalue)) {
								if (!hasIssues) {
									writer.newLine();
									writer.write("MRN: " + mrn);
									writer.newLine();
									writer.write("Issues found:");
									hasIssues = true;
									totalWithIssues++; // Increment issues count
								}

								writer.write(" - Field Name: " + fieldName);
								writer.newLine();
								writer.write("    * File Data: " + fileValue);
								writer.newLine();
								writer.write("    * Default Data: " + defaultvalue);
								writer.newLine();
								writer.write("    * Expected Data: " + defaultvalue);
								writer.newLine();
							}
						}
					}
				}
			}

			// Write final summary
			writer.newLine();
			writer.write("********** Validation Summary **********");
			writer.newLine();
			writer.write("Total MRNs Validated: " + totalValidated);
			writer.newLine();
			writer.write("Total MRNs with Issues: " + totalWithIssues);
			writer.newLine();
			if (totalWithIssues!= 0) {
				writer.write("Test Result: Fail");
				writer.newLine();
			}else {
				writer.write("Test Result: Pass");
				writer.newLine();
			}
			writer.write("===================================");
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
