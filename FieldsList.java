package ExtractAutomation;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FieldsList {
	
	public String MRN;
	public String First_Name;
	public String Middle_Name;
	public String Last_Name;
	public String DOB;
	public String Gender;
	
    public String getMRN() { return MRN; }
    public void setMRN(String mrn) { this.MRN = mrn; }
    
    public String getFirst_Name() { return First_Name; }
    public void setFirst_Name(String First_Name) { this.First_Name = First_Name; }
    
    public String getMiddle_Name() { return Middle_Name; }
    public void setMiddle_Name(String Middle_Name) { this.Middle_Name = Middle_Name; }
    
    
    public String getLast_Name() { return Last_Name; }
    public void setLast_Name(String Last_Name) { this.Last_Name = Last_Name; }
    
    public String getDOB() { return DOB; }
    public void setDOB(String DOB) { this.DOB= DOB; }
    
    public String getGender() { return Gender; }
    public void setGender(String Gender) { this.Gender= Gender; }

    //creating a arraylist to store it
//    public ArrayList<String> toArrayList() {
        ArrayList<String> fieldList = new ArrayList<>();
//    Field[] fields = this.getClass().getDeclaredFields(); // Get all fields declared in the class
//
//    for (Field field : fields) {
//        field.setAccessible(true); // Allow access to private fields
//        try {
//            Object value = field.get(this); // Get field value
//            fieldList.add(value != null ? value.toString() : ""); // Add value to list or empty string if null
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//        fieldList.add(getMRN());
//        fieldList.add(getFirst_Name());
//        fieldList.add(getMiddle_Name());
//        fieldList.add(getLast_Name());
//        fieldList.add(getDOB());
//        fieldList.add(getGender());
//	return fieldList;



}
 
//public ;
