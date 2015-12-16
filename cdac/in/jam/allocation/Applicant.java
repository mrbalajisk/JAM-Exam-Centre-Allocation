package cdac.in.jam.allocation;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;


class DuplicateInfo{

	String enrollment1;
	String paper1;
	
	String enrollment2;
	String paper2;

	DuplicateInfo(String app1, String paper1, String app2, String paper2 ){
		this.enrollment1 = app1;
		this.paper1 = paper1;
		this.enrollment2 = app2;
		this.paper2 = paper2;	
	}

}

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
	Map<String, Boolean> isAllocated;
	Map<String, Session> sessionMap;

	Centre centre;
	City city;
	Session session;
	String firstChoice;

	boolean centreAllocated;
	boolean cityAllocated;

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

		this.firstChoice = new String( choices[0] );

		this.sessionMap = new TreeMap<String, Session>();		
		this.sessionMap.put( this.paperCode1, null );
		if( this.paperCode2 != null )	
			this.sessionMap.put( this.paperCode2, null );

		this.registrationId = new LinkedHashMap<String, String>();
		this.registrationId.put( this.paperCode1, null );	

		if( this.paperCode2 != null )
		this.registrationId.put( this.paperCode2, null );		

		this.isAllocated = new LinkedHashMap<String, Boolean>();
		this.isAllocated.put( this.paperCode1, new Boolean(false) );

		if( this.paperCode2 != null )
		this.isAllocated.put( this.paperCode2, new Boolean(false) );
		

	
		this.centreAllocated = false;
		this.cityAllocated = false;	
		this.centre = null;
		this.city = null;
		this.allotedChoice = -1;	
	}

	static void header( boolean cameraReady ){
		if( cameraReady ){
			System.out.println("application_id, exam_center_id, registration_id1, session, session_date, session_time, exam_city, zone_id, registration_id2, session_time2, disclaimer_accepted, is_provisional");
		}else{
			System.out.println("Zone, Enrollment, Name, Gender, PwD-Status, CentreCode, Allocated-City, PaperCode1, PaperCode2, registrationId1, registrationId2, Session1-date, Session1-time,Session2-time, Provisional-Status, City-Choice1, City-Choice2, City-Choice3, Original-FirstChoice;");
		}
	}

	void print(boolean cameraReady){


		if( cameraReady ){

				if( centre != null ){
					System.out.print(enrollment+","+centre.centreCode+","+registrationId.get( paperCode1 )+","+sessionMap.get( paperCode1 ).sessionId+","+sessionMap.get( paperCode1 ).date+","+sessionMap.get( paperCode1).sessionTime+","+city.cityCode+","+zoneId+","+registrationId.get( paperCode2 ));
						if( paperCode2 != null ){
							System.out.println(","+sessionMap.get( paperCode2).sessionTime+",false,false");
						}else{
							System.out.println(",null,false,false");
						}
				}
		}else{

				if( centre != null ){

					System.out.print("Zone"+zoneId+", "+enrollment+", "+name+", "+gender+", "+isPwD+", "+centre.centreCode+", "+city.cityCode+", "+paperCode1+", "+paperCode2+", "+registrationId.get( paperCode1 )+", "+registrationId.get( paperCode2 )+", ");
						if( paperCode1 != null && paperCode2 != null){
							System.out.println( sessionMap.get( paperCode1 ).date+", "+sessionMap.get( paperCode1).sessionTime+", "+sessionMap.get(paperCode2).sessionTime+", false"+", "+choices[0]+", "+choices[1]+", "+choices[2]+", "+firstChoice);
						}else{	
							System.out.println( sessionMap.get( paperCode1 ).date+", "+sessionMap.get( paperCode1 ).sessionTime+", "+null+", false"+", "+choices[0]+", "+choices[1]+", "+choices[2]+", "+firstChoice);
						}
				}else{
					System.out.println("Zone"+zoneId+", "+enrollment+", "+name+", "+gender+", "+isPwD+", null, null, "+paperCode1+", "+paperCode2+", null, null, null, null, null, false, "+choices[0]+", "+choices[1]+", "+choices[2]+", "+firstChoice );
				}	
		}
	}
} 
