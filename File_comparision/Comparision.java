package ExtractAutomation.File_comparision;

import java.io.IOException;
import java.util.List;

import ExtractAutomation.FileReader;
import ExtractAutomation.TransformationMethods;

public class Comparision {
	public static void main(String[] args) throws IOException {
		List<List<String>> readOutFile = FileReader.readOutFile();
		
		for (List<String> row : readOutFile) {
            System.out.println(row);
		}
	}
	

}
