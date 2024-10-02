package ExtractAutomation.File_comparision;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ExtractAutomation.ConfigReader;
import ExtractAutomation.FileReader;
import ExtractAutomation.JDBCConnection;

public class Comparision {

    public static void main(String[] args) throws IOException {
        // Storing the data from all three files
        Properties properties = ConfigReader.loadproperties();
        String KPMCfilepath = properties.getProperty("file.kpmc.path");
        String LEGACYfilepath = properties.getProperty("file.legacy.path");
        String OUTfilepath = properties.getProperty("file.out.path");

        Map<String, Map<String, String>> kpmcData = FileReader.extractValues(KPMCfilepath);
        Map<String, Map<String, String>> legacyData = FileReader.extractValues(LEGACYfilepath);
        List<List<String>> sortedOutData = FileReader.readAndSortOutFile();

        StringBuilder finalReport = new StringBuilder();
        
        try {
            // Validate KPMC and legacy data and collect the reports
            String legacyReport = validateFileslegacyData(legacyData, sortedOutData);
            String kpmcReport = validateFileskpmcData(kpmcData, sortedOutData);

            // Construct the final report
            finalReport.append("=== FilterMerge Validation Report ===\n");
            finalReport.append("-----------------------------------\n");
            finalReport.append(legacyReport);
            finalReport.append(kpmcReport);
            finalReport.append("=== Summary ===\n");
            finalReport.append("Total OUT File Records: ").append(sortedOutData.size()).append("\n");
            finalReport.append("KPMC Migrated Count + Legacy Non-migrated Count = ")
                .append(getKpmcMigratedCount(kpmcData, sortedOutData) + getLegacyNonMigratedCount(legacyData, sortedOutData)).append("\n");
            finalReport.append("-----------------------------------\n");
        } catch (Exception e) {
            System.out.println("Error found in validateFiles method");
            System.out.println("Error:\n" + e);
        }

        // Write the final report to a file
        String outputFilePath = "F:/Automation Project/Amazon.in/UdemyLearning/src/ExtractAutomation/FilterMergeValidationReport.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(finalReport.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String validateFileskpmcData(Map<String, Map<String, String>> kpmcData, List<List<String>> sortedOutData) throws SQLException, IOException, ClassNotFoundException {
        StringBuilder kpmcReport = new StringBuilder();
        int kpmcMigratedCount = 0;
        int kpmcNonMigratedCount = 0;

        // Collect KPMC report
        kpmcReport.append("== KPMC File ==\n");
        kpmcReport.append("------------------\n");

        for (String compositeKey : kpmcData.keySet()) {
            String mrn = compositeKey.split("-")[0];
            String billingStatus = JDBCConnection.getBillingStatusByMRN(mrn);
            List<String> kpmcRecord = new ArrayList<>(kpmcData.get(compositeKey).values());
            boolean isFound = BinarySearch.binarySearch(kpmcRecord, sortedOutData);

            if ("Migrated".equalsIgnoreCase(billingStatus)) {
                kpmcMigratedCount++;
                if (!isFound) {
                    kpmcReport.append("ERROR: MRN ").append(mrn).append(" is migrated but not found in OUT file.\n");
                    kpmcReport.append("Record: ").append(kpmcRecord).append("\n\n");
                }
            } else {
                kpmcNonMigratedCount++;
                if (isFound) {
                    kpmcReport.append("ERROR: MRN ").append(mrn).append(" is non-migrated but found in OUT file.\n");
                    kpmcReport.append("Record: ").append(kpmcRecord).append("\n\n");
                }
            }
        }

        // Summary for KPMC
        kpmcReport.append("KPMC Migrated Count: ").append(kpmcMigratedCount).append("\n");
        kpmcReport.append("KPMC Non-migrated Count: ").append(kpmcNonMigratedCount).append("\n");
        kpmcReport.append("------------------\n\n");

        return kpmcReport.toString();
    }

    public static String validateFileslegacyData(Map<String, Map<String, String>> legacyData, List<List<String>> sortedOutData) throws ClassNotFoundException, SQLException, IOException {
        StringBuilder legacyReport = new StringBuilder();
        int legacyMigratedCount = 0;
        int legacyNonMigratedCount = 0;

        // Collect Legacy report
        legacyReport.append("== Legacy File ==\n");
        legacyReport.append("--------------------\n");
        System.out.println(legacyData.size());
        for (String compositeKey : legacyData.keySet()) {
            String mrn = compositeKey.split("-")[0];
            String group = compositeKey.split("-")[2];
            String billingStatus = JDBCConnection.getBillingStatusByGroup(group);
            List<String> legacyRecord = new ArrayList<>(legacyData.get(compositeKey).values());
            boolean isFound = BinarySearch.binarySearch(legacyRecord, sortedOutData);
       

            if ("Migrated".equalsIgnoreCase(billingStatus)) {
                legacyMigratedCount++;
                if (isFound) {
                    legacyReport.append("ERROR: MRN ").append(mrn).append(" is migrated but found in OUT file.\n");
                    legacyReport.append("Record: ").append(legacyRecord).append("\n\n");
                }
            } else {
                legacyNonMigratedCount++;
                if (!isFound) {
                    legacyReport.append("ERROR: MRN ").append(mrn).append(" is non-migrated but not found in OUT file.\n");
                    legacyReport.append("Record: ").append(legacyRecord).append("\n\n");
                }
            }
        }

        // Summary for Legacy
        legacyReport.append("Legacy Migrated Count: ").append(legacyMigratedCount).append("\n");
        legacyReport.append("Legacy Non-migrated Count: ").append(legacyNonMigratedCount).append("\n");
        legacyReport.append("--------------------\n\n");

        return legacyReport.toString();
    }

    private static int getKpmcMigratedCount(Map<String, Map<String, String>> kpmcData, List<List<String>> sortedOutData) throws ClassNotFoundException, SQLException, IOException {
        int count = 0;
        for (String compositeKey : kpmcData.keySet()) {
            String billingStatus = JDBCConnection.getBillingStatusByMRN(compositeKey.split("-")[0]);
            if ("Migrated".equalsIgnoreCase(billingStatus)) {
                count++;
            }
        }
        return count;
    }

    private static int getLegacyNonMigratedCount(Map<String, Map<String, String>> legacyData, List<List<String>> sortedOutData) throws ClassNotFoundException, SQLException, IOException {
        int count = 0;
        for (String compositeKey : legacyData.keySet()) {
            String group = compositeKey.split("-")[2];
            String billingStatus = JDBCConnection.getBillingStatusByGroup(group);
            if (!"Migrated".equalsIgnoreCase(billingStatus)) {
                count++;
            }
        }
        return count;
    }
}
