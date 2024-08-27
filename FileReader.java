package ExtractAutomation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import SelfAutomation.PersonRecord;

public class FileReader {
	
	public static Map<String,ArrayList<String>> extractvalues (String filepath) throws IOException
	{
		Map<String,ArrayList<String>> fileValue = new HashMap();
		FileInputStream fstream = new FileInputStream(filepath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        
//        int startindex = startpos-1;
//        int endindex = endpos;
        int rowcount=0;
        String linecount ;
        
        int[][] position = {
        		
        		{1,8}, //MRN pos
        		{9,16},//FIrstname pos
        		{17,20},//Middle Name pos
        		{21,29},//Last Name pos
        		{30,38}, // DOB pos
        		{39,48} // Gender pos
        };
        
        
        
        while((linecount = br.readLine())!= null ) {
        	if(rowcount>2 && rowcount<5) {        		
	        	String MRN = linecount.substring(position[0][0]-1, position[0][1]).trim();
	        	
	        	ArrayList<String> indexList = new ArrayList();
	        	for (int i = 0; i<position.length;i++) {
	        		indexList.add(linecount.substring(position[i][0]-1, position[i][1]).trim());
	        	}
	        	
	        	fileValue.put(MRN,indexList);
        	}        	
        	rowcount++;
        	
        }
        
        
        
        
		
		
		return fileValue;
		
	}

}
