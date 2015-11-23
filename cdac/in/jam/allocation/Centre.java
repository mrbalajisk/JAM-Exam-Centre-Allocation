package cdac.in.jam.allocation;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Centre{

	String centreCode;
	boolean pwdFriendly;
	Map<String, Session> sessionMap;


	Centre( String centreCode, List<String>sessions, String PwDFriendly ){
		this.centreCode = centreCode;
		if( PwDFriendly.equals("YES") )
			pwdFriendly = true;

		this.sessionMap = new TreeMap<String, Session>();
		for(int i = 0, s = 1; i < sessions.size(); i++, s++){
			sessionMap.put( s+"", new Session(s+"", Integer.parseInt( sessions.get(i)  ) ) );
		}
	}
	
	void print(String zone, String cityCode){

		Set<String> sessions = sessionMap.keySet();
		for(String session: sessions){
			sessionMap.get( session ).print(zone, cityCode, centreCode );
			System.out.println();
		}
	}
} 

