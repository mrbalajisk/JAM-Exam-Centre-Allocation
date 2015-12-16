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

		static Map<String, Zone> zoneMap;
		static Map<String, City> cityMap;

		static List<Applicant> applicants;
		static List<Applicant> PwDApplicants;
		static List<Applicant> otherApplicants;
		static List<Applicant> femaleApplicants;
		static List<Applicant> twoPaperApplicants;

		static List<Applicant> notAllocated;
		static List<Applicant> allocated;

		static Map<String, String> paperSessionMap;
		static Map<String, String> applicantCityChange;
		static Map<String, String> cityChange;

		static Map<String, ArrayList<String>> changeCityCentre;
		static Map<String, DuplicateInfo> duplicateList;
		static List<String> notAllocateList;

		static Map<String, Applicant> notAllocatedListApplicant;

		static{
				notAllocatedListApplicant = new TreeMap<String, Applicant>();
				changeCityCentre = new TreeMap<String, ArrayList<String>>();
				duplicateList = new TreeMap<String, DuplicateInfo>();
				notAllocateList = new ArrayList<String>();

				applicants = new ArrayList<Applicant>();
				notAllocated = new ArrayList<Applicant>();
				allocated = new ArrayList<Applicant>();

				PwDApplicants = new ArrayList<Applicant>();
				femaleApplicants = new ArrayList<Applicant>();
				otherApplicants = new ArrayList<Applicant>();
				twoPaperApplicants = new ArrayList<Applicant>();

				zoneMap = new TreeMap<String, Zone>(); 
				cityMap = new TreeMap<String, City>();
				applicantCityChange = new TreeMap<String, String>();
				cityChange = new TreeMap<String, String>();
				paperSessionMap = new TreeMap<String, String>();

				paperSessionMap.put("MA","1");
				paperSessionMap.put("BL","1");
				paperSessionMap.put("PH","1");
				paperSessionMap.put("MS","2");
				paperSessionMap.put("GG","2");
				paperSessionMap.put("BT","2");
				paperSessionMap.put("CY","2");
		}

		void readDuplicate(String filename, boolean withHeader){

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
						String[] tk = line.split(",", -1);
						duplicateList.put( tk[0].trim(), new DuplicateInfo( tk[0].trim(), tk[1].trim(), tk[2].trim(), tk[3].trim() ) );
						notAllocateList.add( tk[2].trim() );
						count++;
					}

					System.err.println("Total duplicate read: "+count) ;
				}catch(Exception e){
					e.printStackTrace();
				}	
		     	
		}

		void readCityChange( String filename, boolean withHeader  ){

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
							String[] tk = line.split(",",-1);

							cityChange.put(tk[0].trim(), tk[1].trim() );					

							ArrayList<String> centres =  new ArrayList<String>();
							String []cts = tk[2].trim().split("-", -1); 
							for(String ct: cts){
								centres.add( ct.trim() );
							}
							changeCityCentre.put( tk[1].trim(), centres );

							count++;
						}
						System.out.println("Number of City-Change Request Read: "+count);
						System.err.println("Number of City-Change Request Read: "+count);

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
		

		void readCandidateCityChocieUpdate( String filename, boolean withHeader  ){

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
							String[] tk = line.split(",",-1);
							applicantCityChange.put(tk[0].trim(), tk[1].trim() );					
							count++;
						}
						System.out.println("Number of Candidate-City-Change Request Read: "+count);
						System.err.println("Number of City-Change Request Read: "+count);
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

								String newChoice = applicantCityChange.get( tk[0].trim() );
								Applicant applicant = null;
								if( newChoice != null){
									applicant = new Applicant( tk[0].trim(), tk[1].trim(), tk[2].trim(), tk[3].trim(), tk[4].trim(), tk[5].trim(), tk[6].trim(), newChoice, tk[8].trim(), tk[9].trim(), tk[10].trim(), tk[7].trim() );
								}else{
									applicant = new Applicant( tk[0].trim(), tk[1].trim(), tk[2].trim(), tk[3].trim(), tk[4].trim(), tk[5].trim(), tk[6].trim(), tk[7].trim(), tk[8].trim(), tk[9].trim(), tk[10].trim(), tk[7].trim() );
								}

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
						System.err.println("Number of Applicant read: "+ applicants.size());	

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
						int count = 0;

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
								List<Session> sessions =  new ArrayList<Session>();
								int i = 11;
								sessions.add( new Session("1", Integer.parseInt( tk[i].trim() ), "February 7 2016 (Sunday)", "Forenoon") );
								i++;
								sessions.add( new Session("2", Integer.parseInt( tk[i].trim() ), "February 7 2016 (Sunday)", "Afternoon") );
								i++;

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

								city.centreMap.put( centerCode, centre );

								/* City Session Capacity Calculation START */
								i = 0;

								for( int s = 1; i < sessions.size(); i++, s++){

										Session session = city.sessionMap.get( s+"" );			
										if( session == null ){
												session = new Session(s+"", sessions.get(i).capacity, sessions.get(i).date, sessions.get(i).sessionTime ) ;
										}else{
												session.capacity +=  sessions.get(i).capacity ;
										}		

										city.sessionMap.put(s+"", session );
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
								count++;
						}
						System.out.println("Number of center read :"+count);	
						System.err.println("Number of center read :"+count);	

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

		void allocate(List<Applicant> applicants, int choiceNumber, boolean onlyFemaleForCity){

				int count = 0;
				int pwdCount = 0;

				for(Applicant applicant: applicants){

						if( applicant.cityAllocated )
							continue;

						if( notAllocateList.contains( applicant.enrollment ) ){
							notAllocatedListApplicant.put( applicant.enrollment, applicant );
							continue;
						}

						if( onlyFemaleForCity && ( cityChange.get( applicant.choices[ choiceNumber ] ) != null && applicant.gender.equals("Male") ) )	
							continue;

						String cityCode = applicant.choices[ choiceNumber ];			

						City city = cityMap.get( cityCode );

						if( city == null){
								System.err.println(cityCode+": City Not found");
								continue;
						}

						if( applicant.paperCode1 != null && applicant.paperCode2 != null ){ // Double Paper

								String session1 = paperSessionMap.get( applicant.paperCode1 );	
								String session2 = paperSessionMap.get( applicant.paperCode2 );

								if( applicant.isPwD  ){
								   if ( !city.isPwdCentreSession.get( session1 ).booleanValue() || ! city.isPwdCentreSession.get( session2 ).booleanValue() )
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

										city.sessionMap.get( session1 ).paperMap.put( applicant.paperCode1, paper );	

										city.sessionMap.get( session2 ).allocated++;

										paper = city.sessionMap.get( session2 ).paperMap.get( applicant.paperCode2 );

										if( paper == null){
												paper = new Paper( applicant.paperCode2, 0 );
										}

										city.sessionMap.get( session2 ).paperMap.put( applicant.paperCode2, paper );	

										applicant.allotedChoice = ( choiceNumber + 1 );

										if( applicant.isPwD ){
												city.sessionMap.get( session1 ).pwdAllocated++;
												city.sessionMap.get( session2 ).pwdAllocated++;
												pwdCount++;
										}
										count++;
										applicant.cityAllocated = true;
								}   				
						}else if ( applicant.paperCode1 != null && applicant.paperCode2 == null ){

								String session1 = paperSessionMap.get( applicant.paperCode1 );	

								if( applicant.isPwD && !city.isPwdCentreSession.get( session1 ).booleanValue() )
									continue;

								if ( ( city.sessionMap.get( session1 ).capacity -  city.sessionMap.get( session1 ).allocated ) > 0 ){  // Single Paper

										city.sessionMap.get( session1 ).allocated++;
										Paper paper = city.sessionMap.get( session1 ).paperMap.get( applicant.paperCode1 );
										if( paper == null){
												paper = new Paper( applicant.paperCode1, 0 );
										}
										paper.applicants.add( applicant );
										city.sessionMap.get( session1 ).paperMap.put( applicant.paperCode1, paper );	
										applicant.allotedChoice = ( choiceNumber + 1);
										if( applicant.isPwD ){
												city.sessionMap.get( session1 ).pwdAllocated++;
												pwdCount++;
										}
										count++;
										applicant.cityAllocated = true;
								}
						}
				}	
		}

		void centreAllocation(){

				Set<String> cityCodes = cityMap.keySet();

				for(String cityCode: cityCodes){

						City city = cityMap.get( cityCode );
						city.allocate();
						city.generateRegistrationId();
				}
		}

		void print( boolean cameraReady ){

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

				Applicant.header( cameraReady );
				for(Applicant applicant: allocated ){
						applicant.print( cameraReady );
				}

				System.out.println("------------------ Not Allocated Candidate ---------------");
				Applicant.header( cameraReady );

				for(Applicant applicant: applicants ){
						if( !allocated.contains( applicant ) ){
								notAllocated.add( applicant );
								applicant.print( cameraReady );
						}	
				}
				System.out.println("Not Allocated candidate "+notAllocated.size());
				System.out.println("Allocated candidate "+allocated.size());
		}

		void cityChangeUpdate(List<Applicant> applicants){

			int count = 0;

			for(Applicant applicant: applicants){

				if( applicant.cityAllocated ){
					continue;

				}else{

					String newChoice = cityChange.get( applicant.choices[0] );

					if( newChoice != null ){
						count++;
						applicant.choices[0] = new String( newChoice );
					}
				}
			}
			System.err.println("No of city-change updated: "+count );
		}

		void allocate(){

			allocate( PwDApplicants, 0, false );
			allocate( twoPaperApplicants, 0, true );
			allocate( applicants, 0, true );

			allocate( PwDApplicants, 0, false );
			allocate( twoPaperApplicants, 0, false );
			allocate( applicants, 0, false );

			cityChangeUpdate( applicants );

			allocate( PwDApplicants, 0, false );
			allocate( twoPaperApplicants, 0, false );
			allocate( applicants, 0, false );


			/*

			int cityAllocated = 0;
			int cityNotAllocated = 0;

			for(Applicant  applicant: applicants){
				if( applicant.cityAllocated )
					cityAllocated++;	
				else
					cityNotAllocated++;
			}

			System.err.println(cityAllocated+" , "+cityNotAllocated);

			*/

		}

		public static void main(String[] args){

				try{
						boolean cameraReady = false;
						int i = 0 ;
						while( i < args.length ){
							if( args[i].equals("-cr") )
								cameraReady = true;
							i++;
						}

						Allocator allocator = new Allocator();
						allocator.readCandidateCityChocieUpdate("./data/candidate-city-change.csv", true );
						allocator.readCityChange("./data/city-change.csv", true );
						//allocator.readCentres("./data/jam-centre-data.csv", true );
						allocator.readCentres("./data/jam-centre-data-161215.csv", true );
						allocator.readDuplicate("./data/duplicate.csv", true);
						allocator.readApplicants("./data/applicant20151126.csv", true );
						allocator.allocate();
						allocator.centreAllocation();
						allocator.print( cameraReady );


				}catch(Exception e){
						e.printStackTrace();
				}
		}
} 

