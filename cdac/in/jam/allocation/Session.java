package cdac.in.jam.allocation;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


class DateTime{

	String date;
	String time;

	DateTime(String date, String time){
		this.date = date;
		this.time = time;
	}

}

public class Session{

	String sessionId;
	String date;
	String sessionTime;
	int capacity;
	int pwdAllocated;
	int allocated;
	int count;

	Map<String, Paper> paperMap;
	List<Applicant> doublePapers;
	List<Applicant> singlePapers;

	static Map<String, DateTime> dateTime = new TreeMap<String, DateTime>();

	static{
		dateTime.put("1", new DateTime("February 7 2016 (Sunday)", "09:00 AM") );	
		dateTime.put("2", new DateTime("February 7 2016 (Sunday)", "02:00 PM") );	
	}

	
	Session(String sessionId, int capacity){

		this.sessionId = sessionId;
		this.capacity = capacity;
		this.pwdAllocated = 0;
		this.allocated = 0;
		this.count = 0;
		this.paperMap = new TreeMap<String, Paper>();

		doublePapers = new ArrayList<Applicant>();
		singlePapers = new ArrayList<Applicant>();
	}

	boolean isFull(){
		if( allocated >= capacity)
			return true;
		return false;	
	}
	
	void print(String zoneCode, String cityCode, String centreCode, String centreName){
		Set<String> papers = paperMap.keySet();
		System.out.print(zoneCode+", "+cityCode+", "+centreCode+", "+centreName+", "+sessionId+", "+capacity+", "+allocated);
		for(String paper: papers){
			paperMap.get( paper ).print();	
		}
	}

	void generateRegistrationId(){
		for(Applicant applicant: doublePapers){
			applicant.registrationId.put( applicant.paperCode1, getRegistration( applicant.centre, "1",  applicant.paperCode1, "B" ) );	
			applicant.registrationId.put( applicant.paperCode2, getRegistration( applicant.centre, "2",  applicant.paperCode2, "B" ) );	
		}

		for(Applicant applicant: singlePapers){
			if( sessionId.equals("1")  )
				applicant.registrationId.put( applicant.paperCode1, getRegistration( applicant.centre, "1",  applicant.paperCode1, "F" ) );	
			else if( sessionId.equals("2") )
				applicant.registrationId.put( applicant.paperCode1, getRegistration( applicant.centre, "2",  applicant.paperCode1, "A" ) );	
		}
	}

	String getRegistration(Centre centre, String sessionId, String paperCode, String displayCode){
			String count = "000"+( centre.sessionMap.get( sessionId ).count + 1 );
			count = count.substring( count.length() - 3);
			centre.sessionMap.get( sessionId ).count++;
			String registrationId = paperCode+""+centre.centreCode+""+displayCode+""+count;
			return registrationId.trim();
	}
} 

