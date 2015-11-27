package cdac.in.jam.allocation;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

public class Session{

	String sessionId;
	int capacity;
	int pwdCapacity;
	int pwdAllocated;
	int allocated;
	int pwdCount;

	Map<String, Paper> paperMap;
	
	Session(String sessionId, int capacity, int pwdCapacity){

		this.sessionId = sessionId;
		this.capacity = capacity;
		this.pwdCapacity = pwdCapacity;
		this.pwdAllocated = 0;
		this.allocated = 0;
		this.pwdCount = 0;
		this.paperMap = new TreeMap<String, Paper>();
	}

	boolean isFull(){
		if( allocated >= capacity)
			return true;
		return false;	
	}
	
	void print(String zoneCode, String cityCode, String centreCode, String centreName){
		Set<String> papers = paperMap.keySet();
		System.out.print(zoneCode+", "+cityCode+", "+centreCode+", "+centreName+", "+sessionId+", "+capacity+", "+allocated);
		for(String paper: papers){
			paperMap.get( paper ).print();	
		}
	}
} 

