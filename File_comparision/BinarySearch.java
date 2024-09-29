package ExtractAutomation.File_comparision;

import java.util.Collections;
import java.util.List;

public class BinarySearch {

	
//	this is working fine 
//	targetRecord should be in the form 756420-Garry-1004 (mrn-name-groupid)
//	if found return the index
//	if not found return -1
	public static boolean binarySearch(List<String> targetRecord, List<List<String>> data) {
        int low = 0;
        int high = data.size() - 1;
        String targetString = "";
        
        for(String temp : targetRecord) {
        	targetString += temp;
        }
        
        while (low <= high) {
            int mid = (low + high) / 2;

            // Compare the MRN at index 0 (or another index based on your key)
            String tempMid = "";
            
            for(String temp : data.get(mid)) {
            	tempMid += temp;
            }

            int comparison = tempMid.compareTo(targetString);

            if (comparison == 0) {
                return true;  // Found the target MRN, return its index
            } else if (comparison < 0) {
                low = mid + 1;  // Target MRN is greater, search the upper half
            } else {
                high = mid - 1;  // Target MRN is smaller, search the lower half
            }
        }

        return false;  // Return -1 if the target MRN is not found
    }
	
	// Method to search for the record in OUT file using binary search(imported from chatGPT)
	public static boolean searchRecordInOutFile(List<String> record, List<List<String>> sortedOutData) {
	    int index = Collections.binarySearch(sortedOutData, record, (record1, record2) -> {
	        for (int i = 0; i < Math.min(record1.size(), record2.size()); i++) {
	            int comparison = record1.get(i).compareTo(record2.get(i));
	            if (comparison != 0) {
	                return comparison;
	            }
	        }
	        return 0; // All fields are equal
	    });
	    return index >= 0; // Return true if record is found
	}

}
