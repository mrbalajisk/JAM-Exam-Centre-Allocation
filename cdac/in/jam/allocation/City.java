package cdac.in.jam.allocation;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.LinkedHashMap;

public class City{

	String cityCode;
	String cityName;
	Map<String, Centre> centreMap;
	Map<String, Session> sessionMap;
	Map<String, Boolean> isPwdCentreSession;
	Map<String, String> paperSessionMap;

	City(String code, String cityName){

		this.cityCode = code;
		this.cityName = cityName;
		this.centreMap = new LinkedHashMap<String, Centre>();
		this.sessionMap = new LinkedHashMap<String, Session>();
		this.isPwdCentreSession = new LinkedHashMap<String, Boolean>();
	}

	void print(String zone){
		Set<String> centres = centreMap.keySet();
		for(String centre: centres ){
			centreMap.get( centre ).print( zone, cityCode );
		}
	}

	void generateRegistrationId(){
		Set<String> centres = centreMap.keySet();
		for(String centre: centres ){
				centreMap.get( centre ).generateRegistrationId();
		}
	}

	void allocate(){
		List<Applicant> doublePaper = new ArrayList<Applicant>();
		List<Applicant> pwd = new ArrayList<Applicant>();
		List<Applicant> singlePaper = new ArrayList<Applicant>();
		List<Applicant> pwdDoublePaper = new ArrayList<Applicant>();

		int i,j,k,l;
		i = j = k = l = 0;	
		Set<String> sessionIds = sessionMap.keySet(); 
		for(String sessionId: sessionIds){

			Session session = sessionMap.get( sessionId );
			boolean flag = true;
			while( flag ){

				flag = false;
				Set<String> paperCodes = session.paperMap.keySet();
				for(String paperCode: paperCodes){

					Paper paper  = session.paperMap.get( paperCode );
					if( paper.applicants.size() > 0 ){

						Applicant applicant = paper.applicants.remove(0);
						flag = true;
						if( applicant.isPwD ){

							if( applicant.paperCode1 != null && applicant.paperCode2 != null ){
								pwdDoublePaper.add(i++, applicant );
							}else{
								pwd.add(j++, applicant );
							}

						}else if ( applicant.paperCode1 != null && applicant.paperCode2 != null ){
								doublePaper.add(k++, applicant );
						}else if ( applicant.paperCode1 != null && applicant.paperCode2 == null ){
								singlePaper.add(l++, applicant );
						}		
					}
				}
			}
		}

		List<Applicant> applicants = new ArrayList<Applicant>();
		int count = 0;
		for(Applicant applicant: pwdDoublePaper){
			if( ! applicants.contains( applicant ) ){
				applicants.add(count++, applicant );
			}	
		}

		for(Applicant applicant: doublePaper){
			if( !applicants.contains( applicant ) ){
				applicants.add(count++, applicant );
			}	
		}

		for(Applicant applicant: pwd){
			if( !applicants.contains( applicant ) ){
				applicants.add(count++, applicant );
			}	
		}

		for(Applicant applicant: singlePaper){
			if( !applicants.contains( applicant ) ){
				applicants.add(count++, applicant );
			}	
		}

		for(Applicant applicant: applicants ){

			Set<String> centreCodes = centreMap.keySet();
			for(String centreCode: centreCodes){

					Centre centre = centreMap.get( centreCode );

					if( applicant.isPwD && !centre.pwdFriendly )
							continue;
					if( Allocator.allocated.contains( applicant ) )
							continue;

					Session session = null;
					if( applicant.paperCode1 != null && applicant.paperCode2 != null){

							if(( centre.sessionMap.get("1").capacity - centre.sessionMap.get("1").allocated ) > 0 																					&& ( centre.sessionMap.get("2").capacity - centre.sessionMap.get("2").allocated ) > 0 ){			

									session =  sessionMap.get( "1" );
									centre.sessionMap.get( session.sessionId ).allocated++;

									session =  sessionMap.get( "2" );
									centre.sessionMap.get( session.sessionId ).allocated++;

									if( applicant.isPwD ){
										centre.sessionMap.get("1").pwdAllocated++;
										centre.sessionMap.get("2").pwdAllocated++;
									}

									applicant.centre =  centre;	
									centre.sessionMap.get( session.sessionId ).doublePapers.add( applicant );
									Allocator.allocated.add( applicant );			

							}
						}else if( applicant.paperCode1 != null && applicant.paperCode2 == null ) {

							session = sessionMap.get( Allocator.paperSessionMap.get( applicant.paperCode1 ) );

							if( ( centre.sessionMap.get( session.sessionId ).capacity - centre.sessionMap.get( session.sessionId ).allocated ) > 0){

								applicant.centre =  centre;	
								centre.sessionMap.get( session.sessionId ).allocated++;

								if( applicant.isPwD ){
										centre.sessionMap.get( session.sessionId ).pwdAllocated++;
								}

								centre.sessionMap.get( session.sessionId ).singlePapers.add( applicant );
								Allocator.allocated.add( applicant );			
							}
						}	
					}
			}
		}
} 

