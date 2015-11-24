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


	public Allocator(){

		applicants = new ArrayList<Applicant>();
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
				Applicant applicant = new Applicant( tk[0].trim(), tk[1].trim(), tk[2].trim(), tk[3].trim(), tk[4].trim(), tk[5].trim(), tk[6].trim(), tk[7].trim(), tk[8].trim(), tk[9].trim() );

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

		try{

			br = new BufferedReader( new FileReader(new File(filename) ) );	

			String line = null;	
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
					city = new City( cityCode );
				}

				city.centreMap.put( centerCode, new Centre( centerCode, sessions, PwDFriendly) );

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
				}

				/* City Session Capacity Calculation  END */

				
				cityMap.put( cityCode, city );	
				zone.cityMap.put( cityCode, city );
				zoneMap.put( zoneId, zone );
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
		
		for(Applicant applicant: applicants){

			if( applicant.isAllocated.get( applicant.paperCode1)  != null && applicant.isAllocated.get( applicant.paperCode1 ) != null ){
				continue;
			}	

			String cityCode = applicant.choices[ choiceNumber ];			
			City city = cityMap.get( cityCode );

			if( city == null){
				System.err.println(cityCode+": City Not found");
				continue;
			}
			

			String session1 = paperSessionMap.get( applicant.paperCode1 );	

			if( applicant.paperCode1 != null && applicant.paperCode2 != null ){ // Double Paper

				String session2 = paperSessionMap.get( applicant.paperCode2 );

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
					paper.applicants.add( applicant );
					city.sessionMap.get( session2 ).paperMap.put( applicant.paperCode2, paper );	

					applicant.isAllocated.put( paper.paperCode, "true" );	
					
					count++;
					
				}   				

			}else if ( (city.sessionMap.get( session1 ).capacity -  city.sessionMap.get( session1 ).allocated ) > 0 ){  // Single Paper

					city.sessionMap.get( session1 ).allocated++;
					Paper paper = city.sessionMap.get( session1 ).paperMap.get( applicant.paperCode1 );
					if( paper == null){
						paper = new Paper( applicant.paperCode1, 0 );
					}
					paper.applicants.add( applicant );
					city.sessionMap.get( session1 ).paperMap.put( applicant.paperCode1, paper );	
					applicant.isAllocated.put( paper.paperCode, "true" );	

					count++;
			}
			
		}	
		System.out.println("Allocated: "+count);
	}

	void allocate(){

		/*
		applicants;
    	PwDApplicants;
    	otherApplicants;
    	femaleApplicants;
    	twoPaperApplicants;
		*/
		
		System.out.print("PwD Allocation: ");
		allocate( PwDApplicants, 0 );
		System.out.print("Two Paper: ");
		allocate( twoPaperApplicants, 0 );
		System.out.print("Others: ");
		allocate( applicants, 0 );

	}

	
	void centerAllocate(City city){

		 Set<String> sessionIds = city.sessionMap.keySet();

		 for(String sessionId: sessionIds ){

			Session session =  city.sessionMap.get( sessionId );

			boolean allocatioDone = false;

			while( allocatioDone ){

				allocatioDone = false;
		
				Set<String> paperCodes 	= session.paperMap.keySet();

				for( String paperCode: paperCodes ){

					 Paper paper = session.paperMap.get( paperCode );
					 
					 Set<String> centreCodes = city.centreMap.keySet();

					 for( String centreCode: centreCodes ){

						Centre centre  = city.centreMap.get( centreCode );
						
						if( ( centre.sessionMap.get( session.sessionId ).capacity - centre.sessionMap.get( session.sessionId ).allocated ) > 0 ){

								if( paper.applicants.size() >  0){

									Applicant applicant = paper.applicants.remove(0);
									allocatioDone = true;
									applicant.centre =  centre;	
									applicant.registrationId.put(paper.paperCode, generateRegistrationId( session, centre, paper ) ) ;
									applicant.isAllocated.put( paper.paperCode, "true");
									applicant.print();
								}
						}
					 }	
				}
			}
		 } 	  		
	}

	String generateRegistrationId(Session session, Centre centre, Paper paper){

		String count = "000"+centre.sessionMap.get( session.sessionId ).allocated + 1;
		count = count.substring( count.length() - 3);
		centre.sessionMap.get( session.sessionId ).allocated++;
		String registrationId = paper.paperCode+"16S"+session.sessionId+""+centre.centreCode+""+count;
	return registrationId.trim();
	}

	void centreAllocation(){

		Set<String> cityCodes = cityMap.keySet();
		for(String cityCode: cityCodes){
			City city = cityMap.get( cityCode );
			System.out.println( "Allocation for "+city.cityCode);
			centerAllocate( city );
		}
	}

	void print(){

	}

	public static void main(String[] args){

		try{

			Allocator allocator = new Allocator();
			allocator.readApplicants("./data/applicant-20151123.csv", true);
			allocator.readCentres("./data/IISc.csv", true);
			allocator.readConstraints("");
			allocator.allocate();
			allocator.centreAllocation();
			allocator.print();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

} 

