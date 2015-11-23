package cdac.in.jam.allocation;

import java.util.List;
import java.util.ArrayList;

public class Paper{

	String code;
	int capacity;
	int allocated;
	int count;	
	List<Applicant> applicants;
	
	Paper(String code, int capacity){
		this.code = code;
		this.capacity = capacity;
		this.applicants = new ArrayList<Applicant>();
		this.allocated = 0;
		this.count = 0;
	}
	
	boolean isFull(){
		if( allocated >= capacity )
			return true;
		return false; 
	}

	void print(){
		System.out.print(", "+capacity+"|"+allocated );
	}
} 

