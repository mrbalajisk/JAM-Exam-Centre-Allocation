package cdac.in.jam.allocation;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Zone{

	String zoneId;
	String zoneName;	

	Map<String, City> cityMap;
	
	Zone(String zoneId, String zoneName ){
	     this.zoneId = zoneId;
		 this.zoneName = zoneName;
	     this.cityMap = new TreeMap<String, City>();
	}
	
	void print(){
		Set<String> cities = cityMap.keySet();
		for(String city: cities){
			cityMap.get( city ).print( zoneId );	
		}			
	}
} 

