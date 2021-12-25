package AIDM_USERS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SplittableRandom;

import AIDM_Miner.AIDM_Parser;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_Utils.AIDM_Utils;




public class AIDM_USER_CHI extends AIDM_USERS  {

	
	HashMap<String,Double> ChiMapping;
	String dataset;
	String support;
	HashMap<AIDM_Pattern,Double> PatternVectors;
	AIDM_Parser parser;
	public AIDM_USER_CHI(String dataset,String support, HashMap<AIDM_Pattern,Double>patternVectors) {
		this.dataset=dataset;
		this.support=support;
		ChiMapping=new HashMap<String, Double>();
		this.parser=new AIDM_Parser();
		this.PatternVectors=patternVectors;
		BuildMapping();
	}

	
	private void BuildMapping() {

		int i =0;
		for(AIDM_Pattern p : PatternVectors.keySet()) {
			ChiMapping.put(p.getShortname(), PatternVectors.get(p));
			i++;
		}
		
		//Normalize(ChiMapping);
		System.out.print("");
	}
	
	private void Normalize(HashMap<String, Double> chiMapping2) {
		ArrayList<Double> values = new ArrayList(chiMapping2.values());
		double min = Collections.min(values);
		double max = Collections.max(values);
			for(String p : chiMapping2.keySet()) {
				double x = chiMapping2.get(p);
				double norm = (x- min)/(max-min);
				chiMapping2.put(p, norm);
			}
		
	}
	
	



	@Override
	public double Compute(AIDM_Pattern p) {
		return ChiMapping.get(p.getShortname());

		
	}
	
	public double getUserValue(String p) {
		
		return ChiMapping.get(p);
		
	}


	@Override
	public ArrayList<Integer> getOrder() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
