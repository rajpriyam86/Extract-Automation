package ExtractAutomation.JobCompletionCheck;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import ExtractAutomation.ConfigReader;
import ExtractAutomation.WinSCPJava;

public class BatchRunCheck {

    public static void main(String[] args) {
        String filePath = "F:/Automation Project/Amazon.in/UdemyLearning/src/ExtractAutomation/JObRun_FileNameconvention.txt"; // Change to .html
        
        // Load database connection properties and fetch job status
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            
            // Start HTML document
            writer.write("********** Test Summary **********");
			writer.newLine();
			writer.write("===================================");
			writer.newLine();
			writer.newLine();

            // Load database connection properties
            Properties properties = ConfigReader.loadproperties();
            String DBUrl = properties.getProperty("db.url");
            String Username = properties.getProperty("db.user");
            String DBpassword = properties.getProperty("db.password");
            String jobStatusQuery = properties.getProperty("sql.query.Job.status"); // SQL query to check job status

            // Register the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Establish connection
            Connection conn = DriverManager.getConnection(DBUrl, Username, DBpassword);
            // Create a statement object to execute queries
            Statement statement = conn.createStatement();

            // Execute the job status query
            ResultSet rs = statement.executeQuery(jobStatusQuery);

            // Check if the result set has at least one record (the latest one)
            if (rs.next()) { // Fetch the first record
                String jobRunId = rs.getString("JOB_RUN_ID"); // Fetch JOB_RUN_ID
                String jobRunStatus = rs.getString("JOB_RUN_STATUS"); // Fetch JOB_RUN_STATUS
                String startTime = rs.getString("START_TIME"); // Fetch START_TIME
                String endTime = rs.getString("END_TIME"); // Fetch END_TIME

                // Write job details to the summary file
            	writer.newLine();
                writer.write("Test result for JobCompletionCheck:");
                writer.newLine();
                writer.write("------------------------------------");
            	writer.newLine();
                writer.write("Job Run ID: " + jobRunId + "");
            	writer.newLine();
                writer.write("Job Run Status: " + jobRunStatus + "");
            	writer.newLine();
                writer.write("Start Time: " + startTime + "");
            	writer.newLine();
                writer.write("End Time: " + endTime + "");
            	writer.newLine();
            	
               

//                System.out.println("Job run completed with status: " + jobRunStatus + "\n");
                
                //putting a condition to fetch the file from winscp only when the job status is in completed status
            	writer.write("===================================");
        		writer.newLine();
        		writer.write("Test result for FileNamingConvention:");
        		writer.newLine();
                writer.write("------------------------------------");
        		writer.newLine();
                if (jobRunStatus.equalsIgnoreCase("COMPLETED")) {
                	String filename = WinSCPJava.fetchAndReadLatestFile();
                	
                	if (filename != null) {
                    	writer.newLine();
                		writer.newLine();
                		writer.write("File found in LZ: " + filename + "\n");
                		writer.newLine();
                		writer.write(WinSCPJava.downloadconfirmation);
                		writer.write(WinSCPJava.FilenotFoundMessage);
                		writer.newLine();
                        
                        // Validate against a specific name
                        String expectedFileName = "KPMC_Test_KPMC.KPMC"; // Change this to the expected file name
                        if (filename.equals(expectedFileName)) {
                        	writer.newLine();
                        	writer.write("File name matches: " + filename + "\n");
                        	writer.newLine();
                        	writer.newLine();
                        	writer.write("Test Result: Pass");
            				writer.newLine();
                        } else {
                        	writer.newLine();
                        	writer.write("File name does not match");
                        	writer.newLine();
                        	writer.write("Expected: "+expectedFileName);
                        	writer.newLine();
                        	writer.write("Found: "+filename );
                        	writer.newLine();
                        	writer.newLine();
                        	writer.write("Test Result: Fail");
            				writer.newLine();
                        	
                        }
                    } else {
                    	writer.newLine();
                    	writer.write("No file found in LZ.\n");
                    	
                    }
                	
                }else {
                	writer.newLine();
                	writer.write("Job is not completed yet");
                	writer.newLine();
                }
                writer.newLine();
            	writer.write("===================================");
            	writer.newLine();
            } else {
                // Specific line in red and bold
                writer.write("No job records found.");
//                System.out.println("No job records available to fetch status.\n");
            }
        
            writer.write("***********End of Test Summary***********");
          

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
