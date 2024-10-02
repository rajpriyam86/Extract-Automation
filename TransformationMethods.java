package ExtractAutomation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransformationMethods {
	
	public static String mrnMethod(String value) {
		
		return value.toUpperCase();
		
		
	}
	
	public static String noMethod(String value) {
		return value;
	}
	
	public static String LastNameMethod(String value) {
		
		
		if (value.equalsIgnoreCase("G")) {
			value = "C1";
		}
		return value;
	}
	
	public static String dbMethod(String dateStr) {
        // Input format
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Desired output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy");
        
        String formattedDate = null;
        try {
            // Parse the input date string to a Date object
            Date date = inputFormat.parse(dateStr);
            // Format the date to the desired format
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
        }
        
        return formattedDate;
    }

}
