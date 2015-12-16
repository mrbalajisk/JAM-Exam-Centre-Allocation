package cdac.in.jam.allocation;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

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

	Session(String sessionId, int capacity, String date, String time ){
		
		this.date = date;
		this.sessionTime = time;
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

			DuplicateInfo dpf = Allocator.duplicateList.get( applicant.enrollment );
			
			if( dpf != null ){

				Applicant applicant2 = Allocator.notAllocatedListApplicant.get( dpf.enrollment2 );
				System.err.println(sessionId+","+applicant.enrollment+", "+ applicant2.paperCode1 +", "+applicant.paperCode2 );

				applicant2.sessionMap.put( applicant2.paperCode1, applicant.sessionMap.get( applicant.paperCode2 ) );
				applicant2.centre = applicant.centre;
				applicant.isAllocated.put( applicant.paperCode2, new Boolean(true) );

				applicant.paperCode2 = null;

				applicant2.city = applicant.city;
				applicant2.centreAllocated = true;
				applicant2.cityAllocated = true;
				applicant2.allotedChoice = applicant.allotedChoice;
				Allocator.allocated.add( applicant2 );

				applicant.registrationId.put( applicant.paperCode1, getRegistration( applicant.centre, "1",  applicant.paperCode1, "F" ) );	
				applicant2.registrationId.put( applicant2.paperCode1, getRegistration( applicant2.centre, "2",  applicant2.paperCode1, "A" ) );
			
			}else{

				applicant.registrationId.put( applicant.paperCode1, getRegistration( applicant.centre, "1",  applicant.paperCode1, "B" ) );	
				applicant.registrationId.put( applicant.paperCode2, getRegistration( applicant.centre, "2",  applicant.paperCode2, "B" ) );	
			}
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

