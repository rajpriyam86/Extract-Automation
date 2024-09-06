package ExtractAutomation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestRun {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
//	FieldsList fields = new FieldsList();
	
	NewDBData db = new NewDBData();
	ArrayList<FieldsList> data = db.extractvalues("452135");
	for( FieldsList dbFields  : data) {
		System.out.println(dbFields.getMRN());
		System.out.println("MRN: " + dbFields.getMRN());
        System.out.println("First Name: " + dbFields.getFirst_Name());
        System.out.println("Middle Name: " + dbFields.getMiddle_Name());
        System.out.println("Last Name: " + dbFields.getLast_Name());
        System.out.println("DOB: " + dbFields.getDOB());
        System.out.println("Gender: " + dbFields.getGender());
        System.out.println();
		
	}
	
	
//	fields.setDOB("1245");
//	fields.setMRN("524");
//	fields.setFirst_Name("Arpan");
//	fields.setMiddle_Name("Kumar");
//	ArrayList<String> fieldList = fields.toArrayList();
//    System.out.println(fieldList);
	
	}

}
