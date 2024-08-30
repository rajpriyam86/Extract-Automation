package ExtractAutomation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class FieldToTest {
	public static ArrayList<Integer> fieldToTestList() throws IOException {
		Properties properties = ConfigReader.loadproperties();

		String str2 = properties.getProperty("field.position.test.string");

		// Step 1: Split the string by comma to get a String array
		String[] stringArray2 = str2.split(",");		
		
		Map<String, Integer> fieldPostionList = FieldDetails.fieldNameListMap();
				
		// Step 2: Create an int array of the same length
		ArrayList<Integer> intArray = new ArrayList<>();

		// Step 3: Parse each element of the String array to an int and store it in the
		// int array
		for (int i = 0; i < stringArray2.length; i++) {
			
			if (fieldPostionList.containsKey(stringArray2[i].toUpperCase().trim())) {
				intArray.add(fieldPostionList.get(stringArray2[i].toUpperCase().trim())); // Convert to int and store
			} else {
				System.out.println("Field Details Missing : " + stringArray2[i].toUpperCase());
				System.out.println("Kindly validate the fields and try again.");
				System.exit(0);
			}
		}
		return intArray;

	}
}
