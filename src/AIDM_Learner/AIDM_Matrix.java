package AIDM_Learner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AIDM_Matrix {

	 Map<String ,Double> Matrix;
	public AIDM_Matrix() {
		Matrix = new HashMap<String ,Double>(); 
	}
	
	public void setValue(double row, String measure1, String measure2) {
		Matrix.put(measure1+"-"+measure2, row);
	}

	@Override
	public String toString() {
		String print="";
		for(String s : Matrix.keySet())
			print+=s+" ==> "+Matrix.get(s)+"\n";
		return print;
	}
	
	
	
	
}
