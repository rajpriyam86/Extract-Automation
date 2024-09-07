package ExtractAutomation;

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

}
