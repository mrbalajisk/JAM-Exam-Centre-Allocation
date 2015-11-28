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
			Session session =  null;
			if( pwdFriendly )	
				sessionMap.put(s+"", new Session(s+"", Integer.parseInt( sessions.get(i) ) ) );
			else
				sessionMap.put(s+"", new Session(s+"", Integer.parseInt( sessions.get(i) ) ) );
		}
	}

	static void header(){
		System.out.println("Zone, CityCode, Centre-Code, CentreName, Pwd-Friendly, Session1(Capacity|Allocated|PwD), Session2( Capacity|Allocated|PwD) ");
	}
	
	void print(String zone, String cityCode){
		System.out.print( zone+", "+cityCode+", "+centreCode+", '"+centreName+"', "+pwdFriendly);
		Set<String> sessionIds = sessionMap.keySet();
		for(String sessionId: sessionIds){
			Session session = sessionMap.get( sessionId );
			System.out.print(", ("+session.capacity+"|"+session.allocated+"|"+session.pwdAllocated+")");
		}
		System.out.println();
	}

	void generateRegistrationId(){
		Set<String> sessionIds = sessionMap.keySet();
		for(String sessionId: sessionIds){
			sessionMap.get( sessionId ).generateRegistrationId();
		}
	}
} 

