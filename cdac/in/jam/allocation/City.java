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

				Set<String> sessionIds = sessionMap.keySet();
				List<Applicant> cityChange = new ArrayList<Applicant>();
				List<Applicant> cityChangeDouble = new ArrayList<Applicant>();
				List<Applicant> doublePaper = new ArrayList<Applicant>();
				List<Applicant> pwdDoublePaper = new ArrayList<Applicant>();
				List<Applicant> pwdSinglePaper = new ArrayList<Applicant>();
				List<Applicant> singlePaper = new ArrayList<Applicant>();

				for(String sessionId: sessionIds ){
						Session session = sessionMap.get( sessionId );
						Set<String> paperCodes = session.paperMap.keySet();
						boolean run = true ;
						while( run ){ 

								run = false;		
								for(String paperCode: paperCodes){

										Paper paper  = session.paperMap.get( paperCode );

										if( paper.applicants.size() > 0 ){

												run = true;

												Applicant applicant = paper.applicants.remove(0);

												if( ! applicant.firstChoice.equals( applicant.choices[0] ) && Allocator.changeCityCentre.get( applicant.choices[0] ) != null  ){
														if( applicant.paperCode1 != null && applicant.paperCode2 != null ){
																cityChangeDouble.add( applicant );
														}else{
																cityChange.add( applicant );
														}

												}else if( applicant.paperCode1 != null && applicant.paperCode2 != null ){

														if( applicant.isPwD ){
																pwdDoublePaper.add( applicant ) ;
														}else{
																doublePaper.add( applicant );
														}	

												}else{
														if( applicant.isPwD ){
																pwdSinglePaper.add( applicant ) ;
														}else{
																singlePaper.add( applicant );
														}	
												}

										}	
								}
						}				
				} 	 	

				List<Applicant> applicants = new ArrayList<Applicant>(); 					

				for(Applicant applicant: cityChangeDouble){
						applicants.add( applicant );
				} 	
				for(Applicant applicant: cityChange){
						applicants.add( applicant );
				} 	

				if( applicants.size() > 0 )
						cityChangeAllocate( applicants );			

				applicants = new ArrayList<Applicant>(); 					

				for(Applicant applicant: pwdDoublePaper ){
						applicants.add( applicant );
				}

				for(Applicant applicant: doublePaper ){
						applicants.add( applicant );
				}	

				for(Applicant applicant: pwdSinglePaper ){
						applicants.add( applicant );
				}	

				for(Applicant applicant: singlePaper ){
						applicants.add( applicant );
				}	

				allocate( applicants );
		}


		void allocate(List<Applicant> applicants ){

				for(Applicant applicant: applicants ){

						if(	applicant.centreAllocated )
								continue; 

						Set<String> centreCodes = centreMap.keySet();

						for(String centreCode: centreCodes){

								if(	applicant.centreAllocated )
										continue; 

								Centre centre = centreMap.get( centreCode );

								if( applicant.isPwD && !centre.pwdFriendly )
										continue;

								Session session = null;

								if( applicant.paperCode1 != null && applicant.paperCode2 != null){

										if(( centre.sessionMap.get("1").capacity - centre.sessionMap.get("1").allocated ) > 0 																					&& ( centre.sessionMap.get("2").capacity - centre.sessionMap.get("2").allocated ) > 0 ){			

												applicant.city = this;	
												session =  sessionMap.get( "1" );
												applicant.sessionMap.put( applicant.paperCode1,  session );

												centre.sessionMap.get( session.sessionId ).allocated++;

												session =  sessionMap.get( "2" );
												applicant.sessionMap.put( applicant.paperCode2,  session );

												centre.sessionMap.get( session.sessionId ).allocated++;

												if( applicant.isPwD ){
														centre.sessionMap.get("1").pwdAllocated++;
														centre.sessionMap.get("2").pwdAllocated++;
												}

												applicant.centre =  centre;	
												centre.sessionMap.get("1").doublePapers.add( applicant );
												//centre.sessionMap.get("2").doublePapers.add( applicant );

												applicant.centreAllocated = true;

												Allocator.allocated.add( applicant );			

										}
								}else if( applicant.paperCode1 != null && applicant.paperCode2 == null ) {

										session = sessionMap.get( Allocator.paperSessionMap.get( applicant.paperCode1 ) );

										if( ( centre.sessionMap.get( session.sessionId ).capacity - centre.sessionMap.get( session.sessionId ).allocated ) > 0){

												applicant.city = this;
												applicant.centre =  centre;	
												applicant.sessionMap.put( applicant.paperCode1,  session );

												centre.sessionMap.get( session.sessionId ).allocated++;

												if( applicant.isPwD ){
														centre.sessionMap.get( session.sessionId ).pwdAllocated++;
												}

												centre.sessionMap.get( session.sessionId ).singlePapers.add( applicant );
												applicant.centreAllocated = true;
												Allocator.allocated.add( applicant );			
										}
								}	
						}

				}
		}

		void cityChangeAllocate( List<Applicant> applicants ){

				ArrayList<String> centres = Allocator.changeCityCentre.get( applicants.get(0).choices[0] );

				System.err.println("Total Citychange: "+applicants.size() +" cityCode:"+applicants.get(0).choices[0] );

				int allocated = 0;

				for(Applicant applicant: applicants){

						Set<String> centreCodes = centreMap.keySet();

						boolean run  = true;

						while( run ){

								run = false;

								for(String centreCode: centreCodes){

										if( applicant.centreAllocated )
											break;

										//System.err.println( centreCode +" , "+centres.contains( centreCode )+", "+centres);

										if( ! centres.contains( centreCode ) ){
												continue;
										}

										Centre centre = centreMap.get( centreCode );

										Session session = null;

										if( applicant.paperCode1 != null && applicant.paperCode2 != null ){

												if(( centre.sessionMap.get("1").capacity - centre.sessionMap.get("1").allocated ) > 0 																					&& ( centre.sessionMap.get("2").capacity - centre.sessionMap.get("2").allocated ) > 0 ){			

														run  = true;

														applicant.city = this;
														session =  sessionMap.get( "1" );
														applicant.sessionMap.put( applicant.paperCode1,  session );
														centre.sessionMap.get( session.sessionId ).allocated++;

														session =  sessionMap.get( "2" );
														applicant.sessionMap.put( applicant.paperCode2,  session );
														centre.sessionMap.get( session.sessionId ).allocated++;

														if( applicant.isPwD ){
																centre.sessionMap.get("1").pwdAllocated++;
																centre.sessionMap.get("2").pwdAllocated++;
														}
														applicant.centre =  centre;	

														centre.sessionMap.get("1").doublePapers.add( applicant );
														//centre.sessionMap.get("2").doublePapers.add( applicant );

														applicant.centreAllocated = true;

														Allocator.allocated.add( applicant );			

														allocated++;

												}

										}else if( applicant.paperCode1 != null && applicant.paperCode2 == null ) {

												session = sessionMap.get( Allocator.paperSessionMap.get( applicant.paperCode1 ) );

												if( ( centre.sessionMap.get(session.sessionId).capacity - centre.sessionMap.get(session.sessionId).allocated ) > 0){

														applicant.city = this;
														run  = true;

														applicant.centre =  centre; 
														applicant.sessionMap.put( applicant.paperCode1,  session );
														centre.sessionMap.get( session.sessionId ).allocated++;

														if( applicant.isPwD ){
																centre.sessionMap.get( session.sessionId ).pwdAllocated++;
														}

														centre.sessionMap.get( session.sessionId ).singlePapers.add( applicant );
														applicant.centreAllocated = true;
														Allocator.allocated.add( applicant );           
														allocated++;
												}
										}   

								} 

						}
				}
				System.err.println("Allocated cityChnage: "+allocated);
		}

}
