package cdac.in.jam.allocation;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Centre{

	String centreCode;
	String centreName;
	boolean pwdFriendly;
	Map<String, Session> sessionMap;

	Centre( String centreCode, String centreName, List<String>sessions, String PwDFriendly ){

		this.centreName = centreName;
		this.centreCode = centreCode;
		if( PwDFriendly.equals("YES") || PwDFriendly.equals("Yes") || PwDFriendly.indexOf("Y") >= 0 || PwDFriendly.indexOf("y") >=0 )
			this.pwdFriendly = true;

		this.sessionMap = new TreeMap<String, Session>();
		for(int i = 0, s = 1; i < sessions.size(); i++, s++){
			Session session =  new Session(s+"", Integer.parseInt( sessions.get(i)  ) );	
			sessionMap.put( s+"", session );
		}
	}
	
	void print(String zone, String cityCode){

		Set<String> sessions = sessionMap.keySet();
		for(String session: sessions){
			sessionMap.get( session ).print(zone, cityCode, centreCode, centreName );
			System.out.println();
		}
	}
} 

