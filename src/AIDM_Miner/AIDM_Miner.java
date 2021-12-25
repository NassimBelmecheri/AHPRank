package AIDM_Miner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import AIDM_Measures.AIDM_Measure;
import AIDM_Pattern.AIDM_Pattern;

public class AIDM_Miner {
	
	public int totalskypatterns=0;
	
	public boolean LaunchMiner(String dataset, String measures, String minsupp) throws Exception {
		Process p;
		Process p1;

		try {
			String[] cmd;
			String[] cmd1;

				cmd = new String[]{ "sh", "Mining/MineMNR.sh", dataset, measures, minsupp,"true" };
			
				cmd1 = new String[]{ "sh", "Mining/Mine.sh", dataset, measures, minsupp };
			
			System.out.println(Arrays.toString(cmd1));
			Runtime rt = Runtime.getRuntime();
			p = rt.exec(cmd1, cmd1);
			p.waitFor();
			int exitVal = p.exitValue();
            System.out.println("Process exitValue: " + exitVal);

            System.out.println(Arrays.toString(cmd));
			Runtime rt1 = Runtime.getRuntime();
			p1 = rt1.exec(cmd, cmd);
			p1.waitFor();
			int exitVal1 = p1.exitValue();
            System.out.println("Process exitValue: " + exitVal1);

            
			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}
	}

	public boolean LaunchMinerFileDataset(String trainingdataset,String dataset, String measures, String minsupp) throws Exception {
		Process p;
		Process p1;
		Process p_;
		Process p1_;
		try {
			String[] cmd;
			String[] cmd1;
			String[] cmd_;
			String[] cmd1_;
				cmd = new String[]{ "Rscript", "Mining/rscript.R", trainingdataset, measures, minsupp};
			
				cmd1 = new String[]{ "Rscript", "Mining/MiningScriptMNRdataset.R", trainingdataset, measures, minsupp,"true"  };
				
				cmd_ = new String[]{ "Rscript", "Mining/rscript.R", dataset, measures, minsupp};
				
				cmd1_ = new String[]{ "Rscript", "Mining/MiningScriptMNRdataset.R", dataset, measures, minsupp,"true"  };
			
			System.out.println(Arrays.toString(cmd1));
			Runtime rt = Runtime.getRuntime();
			p = rt.exec(cmd1, cmd1);
			p.waitFor();
			int exitVal = p.exitValue();
            System.out.println("Process exitValue: " + exitVal);

            System.out.println(Arrays.toString(cmd));
			Runtime rt1 = Runtime.getRuntime();
			p1 = rt1.exec(cmd, cmd);
			p1.waitFor();
			int exitVal1 = p1.exitValue();
            System.out.println("Process exitValue: " + exitVal1);

            System.out.println(Arrays.toString(cmd1_));
			Runtime rt_ = Runtime.getRuntime();
			p_ = rt_.exec(cmd1_, cmd1_);
			p_.waitFor();
			int exitVal_ = p_.exitValue();
            System.out.println("Process exitValue: " + exitVal_);

            System.out.println(Arrays.toString(cmd_));
			Runtime rt1_ = Runtime.getRuntime();
			p1_ = rt1_.exec(cmd_, cmd_);
			p1_.waitFor();
			int exitVal1_ = p1_.exitValue();
            System.out.println("Process exitValue: " + exitVal1_);

			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}
	}
	public static ArrayList<AIDM_Pattern> Preprocess(String measuresnames,String dataset,String n) {

		ArrayList<AIDM_Pattern> Patterns = new ArrayList<AIDM_Pattern>();
		AIDM_Parser parser = new AIDM_Parser();
		ArrayList<String> PatternNames = parser.GetRules(dataset,n);
		ArrayList<double[]> PatternVectors = parser.GetRulesVectors(dataset,n);
		for (int i = 0; i < PatternVectors.size()-1; i++) {
			ArrayList<AIDM_Measure> measures = GetMeasures(PatternVectors.get(i), measuresnames);

			AIDM_Pattern pattern = new AIDM_Pattern(PatternNames.get(i), measures);
			pattern.setShortname("P" + i);
			Patterns.add(pattern);
		}
		//Normalise(Patterns);
		return Patterns;
	}

	public static HashMap<AIDM_Pattern,Double> GetPatterns(String measuresnames,String dataset,String n) {

		HashMap<AIDM_Pattern,Double>Patterns = new HashMap<AIDM_Pattern,Double>();
		AIDM_Parser parser = new AIDM_Parser();
		ArrayList<String> PatternNames = parser.GetRules(dataset,n);
		ArrayList<double[]> PatternVectors = parser.GetRulesVectors(dataset,n);
		if(!PatternNames.isEmpty())
		for (int i = 0; i < PatternVectors.size()-1; i++) {
			if(PatternVectors.get(i).length!=0) {

			ArrayList<AIDM_Measure> measures = GetMeasures(PatternVectors.get(i), measuresnames);
			double chi = PatternVectors.get(i)[PatternVectors.get(i).length-1];
			AIDM_Pattern pattern = new AIDM_Pattern(PatternNames.get(i), measures);
			pattern.setShortname("P" + i);
			Patterns.put(pattern,chi);}
		}
		else
			for (int i = 0; i < PatternVectors.size()-1; i++) {
				if(PatternVectors.get(i).length!=0) {
				ArrayList<AIDM_Measure> measures = GetMeasures(PatternVectors.get(i), measuresnames);
				AIDM_Pattern pattern = new AIDM_Pattern("P"+i, measures);
				double chi = PatternVectors.get(i)[PatternVectors.get(i).length-1];

				pattern.setShortname("P" + i);
				Patterns.put(pattern,chi);
				}
			}
		Normalise(Patterns.keySet());

		return Patterns;
	}
	

	public static ArrayList<AIDM_Measure> GetMeasures(double[] PatternVectors, String measurenames) {
		ArrayList<AIDM_Measure> measures = new ArrayList<AIDM_Measure>();
		String[] names = measurenames.split(" ");
		for (int i = 0; i < names.length-1; i++) {
			AIDM_Measure measure = new AIDM_Measure(names[i], PatternVectors[i]);
			measures.add(measure);
		}
		return measures;

	}
	

	public static void Normalise(Set<AIDM_Pattern> patterns) {
		for(AIDM_Measure m : patterns.iterator().next().getMeasures()) {
			ArrayList <Double>columnvalues = new ArrayList <Double>(); 
			int i=0;
			for(AIDM_Pattern p : patterns) {
				
				columnvalues.add(p.getMeasureValue(m.getName()));
			}
			double min = Collections.min(columnvalues);
			double max = Collections.max(columnvalues);
			
			i=0;
			for(AIDM_Pattern p : patterns) {
				double x = columnvalues.get(i);
				if((max>1&&min==0)||(max>1)||min<0 && max!=min) {
			//double norm = (Math.atan(Math.log(x))/(Math.PI/2)); //
				//double norm = (x-1)/(x+1); //

				double norm = (x- min)/(max-min);

				p.getNormMeasure(m.getName()).setValue(norm);
				}
				else
					p.getNormMeasure(m.getName()).setValue(x);

				i++;
			}
			
		}
		
	}
	public static void Normalise(ArrayList<AIDM_Pattern> patterns) {
		for(AIDM_Measure m : patterns.iterator().next().getMeasures()) {
			ArrayList <Double>columnvalues = new ArrayList <Double>(); 
			int i=0;
			for(AIDM_Pattern p : patterns) {
				
				columnvalues.add(p.getMeasureValue(m.getName()));
			}
			double min = Collections.min(columnvalues);
			double max = Collections.max(columnvalues);
			
			i=0;
			for(AIDM_Pattern p : patterns) {
				double x = columnvalues.get(i);
				if((max>1&&min==0)||(max>1)||min<0 && max!=min) {
			//double norm = (Math.atan(Math.log(x))/(Math.PI/2)); //
				//double norm = (x-1)/(x+1); //

				double norm = (x- min)/(max-min);

				p.getNormMeasure(m.getName()).setValue(norm);
				}
				else
					p.getNormMeasure(m.getName()).setValue(x);

				i++;
			}
			
		}
		
	}

	public void getSkyPatterns(ArrayList<AIDM_Pattern> Patterns,ArrayList<AIDM_Pattern> Patterns_) {
		ArrayList<AIDM_Pattern> dominated = new ArrayList<AIDM_Pattern>();
		HashMap<String,AIDM_Pattern> pt = new HashMap<String,AIDM_Pattern>();

		for(AIDM_Pattern p : Patterns_)
			pt.put(p.getShortname(), p);
		for(int i =0 ; i< Patterns.size();i++){
			double total=0.0;
			double [] measures1= Patterns.get(i).getVectorMeasures();

			for(int j =0 ; j< Patterns.size();j++){
				double sum=0;
				if(i!=j) {
				double [] measures2= Patterns.get(j).getVectorMeasures();


				for(int k = 0; k< measures2.length;k++) {
					if(measures1[k]<measures2[k]) {
						sum++;
						
				}
					
					
			}
				
				if(sum==measures2.length)
					total++;
			
				
				
				}
								
			
			}
			if(total!=0) {
				
				dominated.add(Patterns.get(i));
				pt.get(Patterns.get(i).getShortname()).dominated=true;
			}
			else {
				totalskypatterns++;
			}
		}
		Patterns.removeAll(dominated);
		
	}
}
