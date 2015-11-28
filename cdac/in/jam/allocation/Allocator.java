package cdac.in.jam.allocation;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Set;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;

public class Allocator{

		Map<String, Zone> zoneMap;
		Map<String, City> cityMap;
		Map<String, String> paperSessionMap;

		List<Applicant> applicants;
		List<Applicant> PwDApplicants;
		List<Applicant> otherApplicants;
		List<Applicant> femaleApplicants;
		List<Applicant> twoPaperApplicants;
		List<Applicant> notAllocatedApplicants;
		List<Applicant> allocatedApplicants;

		public Allocator(){

				applicants = new ArrayList<Applicant>();
				notAllocatedApplicants = new ArrayList<Applicant>();
				allocatedApplicants = new ArrayList<Applicant>();

				PwDApplicants = new ArrayList<Applicant>();
				femaleApplicants = new ArrayList<Applicant>();
				otherApplicants = new ArrayList<Applicant>();
				twoPaperApplicants = new ArrayList<Applicant>();

				zoneMap = new TreeMap<String, Zone>(); 
				cityMap = new TreeMap<String, City>();

				paperSessionMap = new TreeMap<String, String>();

				paperSessionMap.put("MA","1");
				paperSessionMap.put("BL","1");
				paperSessionMap.put("PH","1");
				paperSessionMap.put("MS","2");
				paperSessionMap.put("GG","2");
				paperSessionMap.put("BT","2");
				paperSessionMap.put("CY","2");

		}

		void readApplicants(String filename, boolean withHeader){

				if( filename == null || filename.trim().length() == 0)	
						return;

				BufferedReader br =  null; 

				try{

						br = new BufferedReader( new FileReader(new File(filename) ) );	
						String line = null;	
						boolean header = true;
						int count = 0;

						while( ( line =  br.readLine() ) != null ){

								if( withHeader ){
										withHeader = false; 
										continue;
								}

								count++;

								String[] tk = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
								if( tk.length < 10)	
										continue;
								Applicant applicant = new Applicant( tk[0].trim(), tk[1].trim(), tk[2].trim(), tk[3].trim(), tk[4].trim(), tk[5].trim(), tk[6].trim(), tk[7].trim(), tk[8].trim(), tk[9].trim(), tk[10].trim() );

								applicants.add( applicant );	

								if( tk[2].trim().equals("Female") ){
										femaleApplicants.add( applicant );
								}

								if( applicant.paperCode1 != null && applicant.paperCode2 != null ){
										twoPaperApplicants.add( applicant );
								}	

								if( tk[3].trim().equals("t") ){	
										PwDApplicants.add( applicant );	
								}else{
										otherApplicants.add( applicant );		
								}	

								if( count % 10000 == 0){
										System.out.println(count+" Applicant Read!");
								}
						}

						System.out.println(PwDApplicants.size()+" Total PwD Applicant Read!");	
						System.out.println(femaleApplicants.size()+" Total Female Applicant Read!");	
						System.out.println(otherApplicants.size()+" Total other (not PwD) Applicant Read!");	
						System.out.println(twoPaperApplicants.size()+" Total Two Paper Applicant Read!");	
						System.out.println(applicants.size()+" Total Applicant Read!");	

				}catch(Exception e){
						e.printStackTrace();	
				}finally{
						if( br != null){
								try{	
										br.close();
								}catch(Exception e){
										e.printStackTrace();	
								}		
						}		
				}		

		}

		void readCentres(String filename, boolean withHeader){

				if( filename == null || filename.trim().length() == 0)	
						return;
				BufferedReader br =  null; 
				String line = null;	
				try{

						br = new BufferedReader( new FileReader(new File(filename) ) );	

						while( ( line =  br.readLine() ) != null ){

								if( withHeader ){
										withHeader = false; 
										continue;
								}

								String[] tk = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

								String zoneId = tk[0].trim();
								String zoneName = tk[1].trim();
								String cityCode = tk[2].trim();
								String centerCode = tk[3].trim();
								String centerName = tk[4].trim();
								String cityName = tk[9].trim();
								String state = tk[10].trim();

								List<String> sessions =  new ArrayList<String>();
								int i = 11;

								for(int j = 0; i < tk.length; i++, j++){

										try{
												int num = Integer.parseInt( tk[i].trim() );
										}catch(Exception e){
												break;
										}	
										sessions.add( j, tk[i].trim() );
								}

								String PwDFriendly = tk[i].trim();

								Zone zone = zoneMap.get( zoneId );

								if( zone == null){
										zone = new Zone(zoneId, zoneName);
								}

								City city = zone.cityMap.get( cityCode );

								if( city == null){
										city = new City( cityCode, cityName );
								}

								Centre centre = new Centre( centerCode, centerName, sessions, PwDFriendly);

								System.out.println(zone.zoneId+", "+city.cityCode+", "+centre.centreCode);

								city.centreMap.put( centerCode, centre );

								/* City Session Capacity Calculation START */
								i = 0;
								for( int s = 1; i < sessions.size(); i++, s++){

										Session session = city.sessionMap.get( s+"" );			

										if( session == null ){
												session = new Session( s+"", Integer.parseInt( sessions.get(i) ) );
										}else{
												session.capacity += Integer.parseInt( sessions.get(i) ) ;
										}		
										city.sessionMap.put( s+"", session );

										Boolean isPwD = city.isPwdCentreSession.get( session.sessionId );

										if( isPwD == null )
											isPwD = new Boolean(false);
										if( session.capacity > 0 && centre.pwdFriendly )
											isPwD = new Boolean(true);

										city.isPwdCentreSession.put( session.sessionId, isPwD );
								}

								/* City Session Capacity Calculation  END */


								cityMap.put( cityCode, city );	
								zone.cityMap.put( cityCode, city );
								zoneMap.put( zoneId, zone );
						}	

				}catch(Exception e){
						e.printStackTrace();
						System.out.println("Line: "+line);
						System.exit(0);	
				}finally{
						if( br != null){
								try{	
										br.close();
								}catch(Exception e){
										e.printStackTrace();	
								}		
						}		
				}	

		}

		void readConstraints(String filename){

				if( filename == null || filename.trim().length() == 0)	
						return;

				BufferedReader br =  null; 
				try{
						br = new BufferedReader( new FileReader(new File(filename) ) );	
						String line = null;	
						while( ( line =  br.readLine() ) != null ){
								String[] tk = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
						}	

				}catch(Exception e){
						e.printStackTrace();	
				}finally{
						if( br != null){
								try{	
										br.close();
								}catch(Exception e){
										e.printStackTrace();	
								}		
						}		
				}	

		}

		void allocate(List<Applicant> applicants, int choiceNumber){

				int count = 0;
				int pwdCount = 0;

				for(Applicant applicant: applicants){

						if( applicant.paperCode1 != null && applicant.paperCode2 != null ){

							if( applicant.isAllocated.get( applicant.paperCode1 ).equals("true")  && applicant.isAllocated.get( applicant.paperCode2).equals("true") )
										continue;
						}else if ( applicant.paperCode1 != null && applicant.isAllocated.get( applicant.paperCode1 ).equals("true") ){
										continue;
						}

						String cityCode = applicant.choices[ choiceNumber ];			
						City city = cityMap.get( cityCode );

						if( city == null){
								System.err.println(cityCode+": City Not found");
								continue;
						}else{
								System.err.println("City: "+city.cityCode);
						}

						if( applicant.paperCode1 != null && applicant.paperCode2 != null ){ // Double Paper

								String session1 = paperSessionMap.get( applicant.paperCode1 );	
								String session2 = paperSessionMap.get( applicant.paperCode2 );

								if( applicant.isPwD  ){
								   if ( !city.isPwdCentreSession.get( session1 ).booleanValue() || !city.isPwdCentreSession.get( session2 ).booleanValue() )
										continue;
								}
								if( ( city.sessionMap.get( session1 ).capacity -  city.sessionMap.get( session1 ).allocated ) > 0 && 
									( city.sessionMap.get( session2 ).capacity -  city.sessionMap.get( session2 ).allocated ) > 0 ){

										city.sessionMap.get( session1 ).allocated++;

										Paper paper = city.sessionMap.get( session1 ).paperMap.get( applicant.paperCode1 );
										if( paper == null){
												paper = new Paper( applicant.paperCode1, 0 );
										}

										paper.applicants.add( applicant );

										applicant.isAllocated.put( paper.paperCode, "true" );	
										city.sessionMap.get( session1 ).paperMap.put( applicant.paperCode1, paper );	

										city.sessionMap.get( session2 ).allocated++;

										paper = city.sessionMap.get( session2 ).paperMap.get( applicant.paperCode2 );
										if( paper == null){
												paper = new Paper( applicant.paperCode2, 0 );
										}
										city.sessionMap.get( session2 ).paperMap.put( applicant.paperCode2, paper );	
										applicant.isAllocated.put( paper.paperCode, "true" );	
										applicant.allotedChoice = ( choiceNumber + 1);

										if( applicant.isPwD ){
												city.sessionMap.get( session1 ).pwdAllocated++;
												city.sessionMap.get( session2 ).pwdAllocated++;
												pwdCount++;
										}
										count++;
								}   				
						}else if ( applicant.paperCode1 != null && applicant.paperCode2 == null ){

								String session1 = paperSessionMap.get( applicant.paperCode1 );	

								if( applicant.isPwD && !city.isPwdCentreSession.get( session1 ).booleanValue() )
									continue;

								if ( (city.sessionMap.get( session1 ).capacity -  city.sessionMap.get( session1 ).allocated ) > 0 ){  // Single Paper

										city.sessionMap.get( session1 ).allocated++;
										Paper paper = city.sessionMap.get( session1 ).paperMap.get( applicant.paperCode1 );
										if( paper == null){
												paper = new Paper( applicant.paperCode1, 0 );
										}

										paper.applicants.add( applicant );
										city.sessionMap.get( session1 ).paperMap.put( applicant.paperCode1, paper );	
										applicant.isAllocated.put( paper.paperCode, "true" );	
										applicant.allotedChoice = ( choiceNumber + 1);

										if( applicant.isPwD ){
												city.sessionMap.get( session1 ).pwdAllocated++;
												pwdCount++;
										}
										count++;
								}
						}
				}	
				System.out.println("Allocated: "+count+", "+pwdCount);
		}

		void centerAllocate(City city){

				List<Applicant> doublePaper = new ArrayList<Applicant>();
				List<Applicant> pwd = new ArrayList<Applicant>();
				List<Applicant> singlePaper = new ArrayList<Applicant>();
				List<Applicant> pwdDoublePaper = new ArrayList<Applicant>();

				Set<String> sessionIds = city.sessionMap.keySet(); 
				int i,j,k,l;
				i = j = k = l = 0;	

				for(String sessionId: sessionIds){
						Session session = city.sessionMap.get( sessionId );
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

				List<Applicant> cityApplicants = new ArrayList<Applicant>();
				int count = 0;

				for(Applicant applicant: pwdDoublePaper){
						if( !cityApplicants.contains( applicant ) ){
								cityApplicants.add(count++, applicant );
						}	
				}

				for(Applicant applicant: doublePaper){
						if( !cityApplicants.contains( applicant ) ){
								cityApplicants.add(count++, applicant );
						}	
				}

				for(Applicant applicant: pwd){
						if( !cityApplicants.contains( applicant ) ){
								cityApplicants.add(count++, applicant );
						}	
				}

				for(Applicant applicant: singlePaper){
						if( !cityApplicants.contains( applicant ) ){
								cityApplicants.add(count++, applicant );
						}	
				}

				for(Applicant applicant: cityApplicants ){

						Set<String> centreCodes = city.centreMap.keySet();

						for(String centreCode: centreCodes){

								Centre centre = city.centreMap.get( centreCode );

								if( applicant.isPwD && !centre.pwdFriendly )
										continue;

								if( allocatedApplicants.contains( applicant ) )
										continue;

								if( applicant.paperCode1 != null && applicant.paperCode2 != null){

										if(( centre.sessionMap.get("1").capacity - centre.sessionMap.get("1").allocated ) > 0 																					&& ( centre.sessionMap.get("2").capacity - centre.sessionMap.get("2").allocated ) > 0 ){			


												Session session =  city.sessionMap.get( "1" );
												centre.sessionMap.get( session.sessionId ).allocated++;
										
												session =  city.sessionMap.get( "2" );
												centre.sessionMap.get( session.sessionId ).allocated++;
											

												if( applicant.isPwD ){
														centre.sessionMap.get("1").pwdAllocated++;
														centre.sessionMap.get("2").pwdAllocated++;
												}

												applicant.centre =  centre;	
												centre.sessionMap.get( session.sessionId ).doublePapers.add( applicant );
												allocatedApplicants.add( applicant );			

										}
								}else if( applicant.paperCode1 != null && applicant.paperCode2 == null ) {
										Session session = city.sessionMap.get( paperSessionMap.get( applicant.paperCode1 ) );
										if((centre.sessionMap.get( session.sessionId ).capacity - centre.sessionMap.get( session.sessionId ).allocated ) > 0){

												applicant.centre =  centre;	
												centre.sessionMap.get( session.sessionId ).allocated++;

												if( applicant.isPwD ){
														centre.sessionMap.get( session.sessionId ).pwdAllocated++;
												}

												centre.sessionMap.get( session.sessionId ).singlePapers.add( applicant );
												allocatedApplicants.add( applicant );			
										}
								}	
						}
				}
		}

		String generateRegistrationId(Session session, Centre centre, Paper paper, String sessionCode){
				String count = "000"+( centre.sessionMap.get( session.sessionId ).allocated + 1 );
				count = count.substring( count.length() - 3);
				centre.sessionMap.get( session.sessionId ).allocated++;
				String registrationId = paper.paperCode+""+centre.centreCode+""+sessionCode+""+count;
				return registrationId.trim();
		}

		void centreAllocation(){

				Set<String> cityCodes = cityMap.keySet();

				for(String cityCode: cityCodes){
						City city = cityMap.get( cityCode );
						System.err.println( "Allocation for "+city.cityCode+" | "+city.cityName);
						centerAllocate( city );
						city.generateRegistrationId();
				}
		}

		void print( ){

				Set<String> zones = zoneMap.keySet();
				Centre.header();
				for(String zoneId: zones){
						Zone zone = zoneMap.get( zoneId );
						Set<String> cities = zone.cityMap.keySet();
						for(String cityCode: cities){
								City city = cityMap.get( cityCode );
								city.print( zone.zoneId );
						}
				}

				Applicant.header();
				for(Applicant applicant: allocatedApplicants ){
						applicant.print();
				}
				System.out.println("------------------ Not Allocated Candidate ---------------");
				Applicant.header();
				for(Applicant applicant: applicants ){
						if( !allocatedApplicants.contains( applicant ) ){
								notAllocatedApplicants.add( applicant );
								applicant.print();
						}	
				}
				System.out.println("Not Allocated candidate "+notAllocatedApplicants.size());
				System.out.println("Allocated candidate "+allocatedApplicants.size());
		}

		void allocate(){
				int i = 0;	
				while( i < 1){ 
					System.out.print("PwD Allocation: ");
					allocate( PwDApplicants, i );
					System.out.print("Two Paper: ");
					allocate( twoPaperApplicants, i );
					System.out.print("Others: ");
					allocate( applicants, i );
					i++;
				}
		}

		public static void main(String[] args){

				try{

						Allocator allocator = new Allocator();
						allocator.readApplicants("./data/applicant20151126.csv", true);
						allocator.readCentres("./data/jam-centre-data.csv", true);
						//allocator.print();
						allocator.readConstraints("");
						allocator.allocate();
						allocator.centreAllocation();
						allocator.print();


				}catch(Exception e){
						e.printStackTrace();
				}
		}

} 

