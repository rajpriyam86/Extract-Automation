package ExtractAutomation;

import java.lang.reflect.Method;
import java.util.Map;

public class ApplyTransformation {
	 public static String applyTransformation(String fieldName, String value, Map<String, String> fieldTransformations) throws Exception {
	        String methodName = fieldTransformations.get(fieldName);
	        
	        if (methodName == null|| methodName.equalsIgnoreCase("null")) {
//	            throw new IllegalArgumentException("No transformation method found for field: " + fieldName);
	            return value;
	        }

	        // Use reflection to get the method from TransformationMethods and invoke it
	        Method method = TransformationMethods.class.getDeclaredMethod(methodName, String.class);
	        return (String) method.invoke(null, value); // Static method, so no need for an instance
	    }

}