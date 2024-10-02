package ExtractAutomation;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.SftpATTRS;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class WinSCPJava {
	public static String downloadconfirmation= "";
	public static String FilenotFoundMessage = "";
    private static final String SFTP_HOST = "localhost";
    private static final int SFTP_PORT = 22; // Default SFTP port
    private static final String SFTP_USER = "desktop-f0ikbet\\dell";
    private static final String SFTP_PASSWORD = "Priyam@123";
    private static final String REMOTE_DIRECTORY = "/F:/Automation Project/WinSCP";
    private static final String LOCAL_TEMP_PATH = "C:/Users/Dell/Documents/";

    /**
     * Fetches the latest file from the hardcoded SFTP server directory and reads its contents.
     *
     * @return the contents of the latest file
     * @throws Exception if any error occurs during SFTP operations
     */
    public static String fetchAndReadLatestFile() throws Exception {
    	
    	String filename = null;

        Session session = null;
        ChannelSftp channelSftp = null;
        String latestFileContents = "";

        try {
            // Connect to the SFTP server
            JSch jsch = new JSch();
            session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);

            // Disable strict host key checking for simplicity (not recommended for production)
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            System.out.println("Connected to SFTP server.");

            // Open the SFTP channel
            Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            // List files in the remote directory
            Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls(REMOTE_DIRECTORY);

            // Find the latest file based on modification time
            ChannelSftp.LsEntry latestFile = null;
            long latestModifiedTime = Long.MIN_VALUE;

            for (ChannelSftp.LsEntry file : fileList) {
                SftpATTRS attrs = file.getAttrs();
                if (!attrs.isDir()) { // Ignore directories
                    long modifiedTime = attrs.getMTime() * 1000L; // Convert to milliseconds
                    if (modifiedTime > latestModifiedTime) {
                        latestModifiedTime = modifiedTime;
                        latestFile = file;
                    }
                }
            }

            if (latestFile != null) {
                String latestFileName = latestFile.getFilename();
                String remoteFilePath = REMOTE_DIRECTORY + "/" + latestFileName;

                // Download the latest file to the local directory
                File localFile = new File(LOCAL_TEMP_PATH + latestFileName);
                try (FileOutputStream fos = new FileOutputStream(localFile)) {
                    channelSftp.get(remoteFilePath, fos);
                }
                downloadconfirmation ="Latest file downloaded in: " + localFile.getAbsolutePath();

                // Read the file contents
//                latestFileContents = new String(Files.readAllBytes(localFile.toPath()));
                filename = latestFileName;
            } else {
                FilenotFoundMessage = "No files found in the directory.";
            }

        } finally {
            // Disconnect from the SFTP server
            if (channelSftp != null) channelSftp.disconnect();
            if (session != null) session.disconnect();
        }

        return filename;
    }
}
