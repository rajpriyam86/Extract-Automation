package ExtractAutomation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JobRunStatucCheck {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub

		String job_id = "262";
		System.out.println(job_id);

		ResultSet rs = JDBCConnection.JobRunStatus(job_id);

		rs.next();
		try {
			String jobRunStatus = rs.getString("JOB_RUN_STATUS");
			System.out.println("Latest Job Run Status: " + jobRunStatus);
			Assert.assertTrue(jobRunStatus.equalsIgnoreCase("Completed"));
		} catch (AssertionError e) {
			System.out.println("Assertion failed: " + e.getMessage());
			System.err.println("Expected: 'Completed'");
			System.err.println("Actual: '" + rs.getString("JOB_RUN_STATUS") + "'");
			throw e;
		}

	}

}
