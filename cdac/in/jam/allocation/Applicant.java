package cdac.in.jam.allocation;

public class Applicant{

	String enrollment;
	String name;
	String gender;	
	String paperCode1;
	String paperCode2;
	boolean isPwD;
	boolean isScribeReq;
	String[] choices;
	boolean isAllocated;

	String registrationId;
	Centre centre;
	Session session;
	int allotedChoice;

	Applicant(String enrollment, String name, String gender, String isPD, String isScribe, String paperCode1, String paperCode2, String choice1, String choice2, String choice3){

		this.enrollment = enrollment;
		this.name = name;
		this.gender = gender;
		this.isAllocated = false;

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
			
		
		this.registrationId = null;
		this.centre = null;
		this.session = null;	
		this.allotedChoice = -1;	
	}
} 

