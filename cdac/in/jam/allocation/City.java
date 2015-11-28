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

} 

