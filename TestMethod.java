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
        // Define file path
        String outputfile = "F:/Automation Project/Amazon.in/UdemyLearning/src/ExtractAutomation/TestSummary.txt";
        int j = 1; // Creating this variable to print the MRN in output in sequence like 1), 2), 3) this way 

        // Initiating BufferedWriter to print the output in a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile))) {
            writer.write("********** Test Summary **********");
            writer.newLine();
            writer.write("===================================");
            writer.newLine();

            // Read file data
            Map<String, Map<String, String>> fileData = FileReader.extractValues();

            // Compare with DB data
            for (Map.Entry<String, Map<String, String>> entry : fileData.entrySet()) {
                String mrn = entry.getKey();
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
                        writer.newLine();
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
