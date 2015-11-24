package cdac.in.jam.allocation;
import java.util.Map;
import java.util.LinkedHashMap;

public class Applicant{

	String enrollment;
	String name;
	String gender;	
	String paperCode1;
	String paperCode2;
	boolean isPwD;
	boolean isScribeReq;
	String[] choices;

	Map<String, String> registrationId;
	Map<String, String> isAllocated;
	Centre centre;
	Session session;
	int allotedChoice;

	Applicant(String enrollment, String name, String gender, String isPD, String isScribe, String paperCode1, String paperCode2, String choice1, String choice2, String choice3){

		this.enrollment = enrollment;
		this.name = name;
		this.gender = gender;

		this.paperCode1 = paperCode1;
		if( paperCode2 != null && paperCode2.trim().length() > 0)
			this.paperCode2 = paperCode2;
		else	
			this.paperCode2 = null;

		if( isPD.equals("t") )
			this.isPwD = true;
		if( isScribe.equals("t") )
			this.isScribeReq = true;

		this.choices = new String[3];
		this.choices[0] = choice1;
		this.choices[1] = choice2;
		this.choices[2] = choice3;
		
		this.registrationId = new LinkedHashMap<String, String>();
		this.registrationId.put( this.paperCode1, null );	
		this.registrationId.put( this.paperCode2, null );		

		this.isAllocated = new LinkedHashMap<String, String>();
		this.isAllocated.put( this.paperCode1, null);
		this.isAllocated.put( this.paperCode2, null);
		
	
		this.centre = null;
		this.session = null;	
		this.allotedChoice = -1;	
	}

	void print(){
		System.out.println(enrollment+", "+paperCode1+", "+paperCode2+", "+registrationId.get( paperCode1 )+", "+registrationId.get( paperCode2 ) );
	}
} 

