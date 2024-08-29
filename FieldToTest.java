package ExtractAutomation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class FieldToTest {
	public static ArrayList<Integer> fieldToTestList() throws IOException{
		Properties properties = ConfigReader.loadproperties();
		
		String str = properties.getProperty("field.postion.test");
		
		// Step 1: Split the string by comma to get a String array
        String[] stringArray = str.split(",");
        
        // Step 2: Create an int array of the same length
        ArrayList<Integer> intArray = new ArrayList();
        
        // Step 3: Parse each element of the String array to an int and store it in the int array
        for (int i = 0; i < stringArray.length; i++) {
            intArray.add( Integer.parseInt(stringArray[i].trim())); // Convert to int and store
        }
		
		return intArray;
		
	}
}
