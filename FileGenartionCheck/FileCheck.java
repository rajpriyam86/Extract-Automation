package ExtractAutomation.FileGenartionCheck;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ExtractAutomation.WinSCPJava;

public class FileCheck {
    public static void main(String[] args) {
        try {
            String filename = WinSCPJava.fetchAndReadLatestFile();
            String summaryOutput;
            if (filename != null) {
                summaryOutput = "File found in LZ: " + filename + "\n";
                
                // Validate against a specific name
                String expectedFileName = "KPMC_Test_KPMC.KPMC"; // Change this to the expected file name
                if (filename.equals(expectedFileName)) {
                    summaryOutput += "File name matches: " + filename + "\n";
                } else {
                    summaryOutput += "File name does not match. Found: " + filename + "\n";
                }
            } else {
                summaryOutput = "No file found in LZ.\n";
            }
            writeToFile(summaryOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String output) {
        String filePath = "F:/Automation Project/Amazon.in/UdemyLearning/src/ExtractAutomation/TestSummary.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(output);
//            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
