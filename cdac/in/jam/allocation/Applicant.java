package cdac.in.jam.allocation;
import java.util.Map;
import java.util.LinkedHashMap;

public class Applicant{

	String enrollment;
	String name;
	String gender;	
	String paperCode1;
	String paperCode2;
	String zoneId;	
	boolean isPwD;
	boolean isScribeReq;
	String[] choices;

	Map<String, String> registrationId;
	Map<String, String> isAllocated;
	Centre centre;
	Session session;
	String originalFirstChoice;
	int allotedChoice;

	Applicant(String enrollment, String name, String gender, String isPD, String isScribe, String paperCode1, String paperCode2, String choice1, String choice2, String choice3, String zoneId, String originalFirstChoice){

		this.enrollment = enrollment;
		this.name = name;
		this.gender = gender;
		this.zoneId = zoneId;

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

		this.originalFirstChoice = originalFirstChoice;
		
		this.registrationId = new LinkedHashMap<String, String>();
		this.registrationId.put( this.paperCode1, null );	
		this.registrationId.put( this.paperCode2, null );		

		this.isAllocated = new LinkedHashMap<String, String>();
		this.isAllocated.put( this.paperCode1, "false");
		this.isAllocated.put( this.paperCode2, "false");
		
		this.centre = null;
		this.session = null;	
		this.allotedChoice = -1;	
	}

	static void header(){
		System.out.println("Zone, Enrollment, Name, Gender, PwD-Status, CentreCode, PaperCode1, PaperCode2, registrationId1, registrationId2, Session1-date, Session1-time,Session2-time, Provisional-Status, City-Choice1, City-Choice2, City-Choice3, OriginalFirstChoice;");
	}

	void print(){
		if( centre != null )
			System.out.println("Zone"+zoneId+", "+enrollment+", "+name+", "+gender+", "+isPwD+", "+centre.centreCode+", "+paperCode1+", "+paperCode2+", "+registrationId.get( paperCode1 )+", "+registrationId.get( paperCode2 )+", "+session.dateTime.get("1").date+", "+session.dateTime.get("1").time+", "+session.dateTime.get("2").time+", false"+", "+choices[0]+", "+choices[1]+", "+choices[2]+", "+originalFirstChoice);
		else
			System.out.println("Zone"+zoneId+", "+enrollment+", "+name+", "+gender+", "+isPwD+", null, "+paperCode1+", "+paperCode2+", null, null, null, null, null, false, "+choices[0]+", "+choices[1]+", "+choices[2]+", "+originalFirstChoice );
	}
} 
