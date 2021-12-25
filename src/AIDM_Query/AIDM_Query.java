package AIDM_Query;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import AIDM_Measures.AIDM_Measure;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_USERS.AIDM_USERS;
import AIDM_Utils.AIDM_Utils;

public class AIDM_Query {

	public ArrayList<AIDM_Pattern> Patterns;
	public int querysize;
	public AIDM_Query() {

		Patterns = new ArrayList<AIDM_Pattern>();
	}
	public AIDM_Query(int querysize) {
		Patterns = new ArrayList<AIDM_Pattern>();
		this.querysize=Patterns.size();

	}
	public void AddPattern(AIDM_Pattern p) {
		Patterns.add(p);
	}

	public void removePattern(AIDM_Pattern p) {
		Patterns.remove(p);
	}

	public ArrayList<AIDM_Measure> getMeasures() {
		return Patterns.get(0).getMeasures();
	}

	public double[] getRanking(AIDM_Measure m) {
		double[] ranking = new double[Patterns.size()];
		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.getRank(m);
			i++;
		}

		return ranking;
	}

	public double[] getUserRanking() {
		Collections.sort(Patterns, (o1, o2) -> o1.getShortname().compareTo(o2.getShortname()));

		double[] ranking = new double[Patterns.size()];
		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.getUserRank();
			i++;
		}

		return ranking;
	}
	public double[] getUserValues(AIDM_USERS Oracle) {
		Collections.sort(Patterns, (o1, o2) -> o1.getShortname().compareTo(o2.getShortname()));

		double[] ranking = new double[Patterns.size()];
		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = Oracle.Compute(p);
			i++;
		}

		return ranking;
	}
	public double[] getlocalRanking(AIDM_Measure m) {
		double[] ranking = new double[Patterns.size()];
		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.getRank(m);
			i++;
		}
		
		return ranking;
	}

	public double[] getlocalUserRanking() {
		double[] ranking = new double[Patterns.size()];
		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.getUserRank();
			i++;
		}

		return ranking;
	}
	double normalize(double value, double min, double max) {
	    return 1 - ((value - min) / (max - min));
	}
	public double[] getPredictionRanking() {
		double[] ranking = new double[Patterns.size()];
		Collections.sort(Patterns, (o1, o2) -> o1.getShortname().compareTo(o2.getShortname()));

		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.getPredictedRank();
			i++;
		}

		return ranking;
	}
	public double[] getPredictionAHPValues(double[]weights) {
		double[] ranking = new double[Patterns.size()];
		Collections.sort(Patterns, (o1, o2) -> o1.getShortname().compareTo(o2.getShortname()));

		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.PredictScore(weights);
			i++;
		}

		return ranking;
	}
	public double[] getPredictionSVMValues(double[]weights) {
		double[] ranking = new double[Patterns.size()];
		Collections.sort(Patterns, (o1, o2) -> o1.getShortname().compareTo(o2.getShortname()));

		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.PredictScore(weights);
			i++;
		}

		return ranking;
	}

	public double[] getPredictionSVMRanking() {
		double[] ranking = new double[Patterns.size()];
		Collections.sort(Patterns, (o1, o2) -> o1.getShortname().compareTo(o2.getShortname()));

		int i = 0;
		for (AIDM_Pattern p : Patterns) {
			ranking[i] = p.getPredictedSVMRank();
			i++;
		}

		return ranking;
	}

	public void Rank(String m) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();
		for (AIDM_Pattern p : Patterns) {
			patterns.put(p, getMeasureValue(p.getMeasures(), m));
		}
		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(patterns);

		int i = 1;
		for (AIDM_Pattern p : sorted.keySet()) {
			p.setRank(m, i);
			i++;

		}

	}

	public void UserRanking(AIDM_USERS oracle) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();
		double score = oracle.Compute(Patterns.get(0));
		double score1 = oracle.Compute(Patterns.get(1));
		
		for (AIDM_Pattern p : Patterns) {
			
			 score = oracle.Compute(p);
			//System.out.println(p.getShortname()+"&"+score);
			patterns.put(p, score);
			
		}

		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(patterns);
		ArrayList<AIDM_Pattern> keys = new ArrayList(sorted.keySet());
		
		int i = 1;
		for (AIDM_Pattern p : keys) {

			p.setRank("oracle", i);
			i++;

		}

		
	}
	public void NoisyUserRanking(AIDM_USERS oracle) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();
		for (AIDM_Pattern p : Patterns) {
			
			double score = oracle.Compute(p);
			//System.out.println(p.getShortname()+"&"+score);
			patterns.put(p, score);
			
		}

		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(patterns);
		ArrayList<AIDM_Pattern> keys = new ArrayList(sorted.keySet());
		int i = keys.size();
		for (AIDM_Pattern p : keys) {

			p.setRank("oracle", i);
			i--;

		}

		
	}
	public void UserRankingFinal(AIDM_USERS oracle) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();
		for (AIDM_Pattern p : Patterns) {
			
			double score = oracle.Compute(p);
			//System.out.println(p.getShortname()+"&"+score);
			patterns.put(p, score);
			
		}

		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(patterns);
		int i = 1;
		for (AIDM_Pattern p : sorted.keySet()) {

			p.setRank("oracle", i);
			i++;
			

		}

		
	}

	public double getMeasureValue(ArrayList<AIDM_Measure> measures, String m) {
		double measurevalues = 0.0;

		for (AIDM_Measure m1 : measures) {
			if (m1.getName().contains(m)) {
				measurevalues = m1.getValue();
			}

		}
		return measurevalues;
	}

	public double getUserValue(ArrayList<AIDM_Measure> measures) {
		double measurevalues = 0.0;

		for (AIDM_Measure m : measures) {
			if (m.getName().contains("oracle")) {
				measurevalues = m.getValue();
			}

		}
		return measurevalues;
	}

	public ArrayList<AIDM_Pattern> TopKPatterns(String m, int k) {
		ArrayList<AIDM_Pattern> topk = new ArrayList<AIDM_Pattern>();

		for (AIDM_Pattern p : this.Patterns) {

			if (p.getRankByMeasureName(m) < k) {
				topk.add(p);
			}
		}
		return topk;
	}

	@Override
	public String toString() {
		String s = "[";
		for(AIDM_Pattern p : Patterns)
			s+= p.getShortname()+ "|";
		return s +"]";
	}

	public AIDM_Query copy() {
		AIDM_Query copy = new AIDM_Query();
		for (AIDM_Pattern p : this.Patterns) {
			
			AIDM_Pattern p_= new AIDM_Pattern(p.getName(), p.getMeasures());
			p_.setShortname(p.getShortname());
			copy.AddPattern(p_);
		}
		return copy;
	}

	public AIDM_Pattern getPattern(AIDM_Pattern p) {
		for	(AIDM_Pattern p_: this.Patterns) {
			if(p.getShortname().equals(p_.getShortname()))
				return p_;
		}	
		return null;
	}	
	public AIDM_Pattern getPattern(String p) {
		for	(AIDM_Pattern p_: this.Patterns) {
			if(p.equals(p_.getShortname()))
				return p_;
		}	
		return null;
	}
	public void sortByRank() {
				Collections.sort(Patterns);
	}
	public void KeepSignificant(int significant) {
		int x = (this.Patterns.size()-significant);
		Collections.shuffle(this.Patterns);
		AtomicInteger index = new AtomicInteger(0);
	    this.Patterns.removeIf(p ->  index.getAndIncrement() < x);
		
	}
	public void UserRankingSP() {
		HashMap <AIDM_Pattern, Double> rankings = new HashMap<AIDM_Pattern, Double>();
		for(int i =0 ; i< Patterns.size();i++){
			 String name = Patterns.get(i).getName().replaceAll("\\{", "");
			  name = name.replaceAll("}", "");
			  name = name.replaceAll("\"", "");
			  name = name.replaceAll(",", "  ");

			  name = name.replaceAll("=>", "");
			  String [] items = name.split("  ");
			rankings.put(Patterns.get(i), (double)items.length);

		}
	
		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(rankings);
		System.out.print("");
		int i =1;
		for(AIDM_Pattern pn : rankings.keySet()) {
			pn.setRank("oracle", i);
			i++;
			
		}
		System.out.print("");
		
	}	
	public void PrintTopKPatternsAHP(String dataset,int k) {
		try {
		    String directoryName = "Mined";
		    File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }

			FileWriter fr = new FileWriter("Mined/"+dataset+"_ahp_"+k+".csv");
			fr.write("Name\tUserRank\tAHPRank\tSVMRank\n");

			for(AIDM_Pattern p : Patterns)
				if(p.getPredictedRank()<=k)
					fr.write(p.getName()+"\t"+p.getUserRank()+"\t"+p.getPredictedRank()+"\t"+p.getPredictedSVMRank()+"\n");
			
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public void PrintTopKPatternsSVM(String dataset,int k) {
		try {
		    String directoryName = "Mined";
		    File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }

			FileWriter fr = new FileWriter("Mined/"+dataset+"_svm_"+k+".csv");
			fr.write("Name\tUserRank\tAHPRank\tSVMRank\n");

			for(AIDM_Pattern p : Patterns)
				if(p.getPredictedSVMRank()<=k)
					fr.write(p.getName()+"\t"+p.getUserRank()+"\t"+p.getPredictedRank()+"\t"+p.getPredictedSVMRank()+"\n");
			
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void PrintScoredPatternsAHP(String dataset,double []weights,AIDM_USERS user) {
		try {
		    String directoryName = "Mined";
		    File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }

			FileWriter fr = new FileWriter("Mined/"+dataset+"_ahp_scores_all"+".csv");
			fr.write("Name\tScore\tUser\tSkyPattern\n");

			for(AIDM_Pattern p : Patterns)
				if(user!=null)
				if(p.dominated==true)
					fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+user.Compute(p)+"\t"+0+"\n");
				else
					fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+user.Compute(p)+"\t"+1+"\n");
				else
					if(p.dominated==true)
						fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+p.getUserRank()+"\t"+0+"\n");
					else
						fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+p.getUserRank()+"\t"+1+"\n");
					
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void PrintScoredPatternsSVM(String dataset,double []weights,AIDM_USERS user) {
		try {
		    String directoryName = "Mined";
		    File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }

			FileWriter fr = new FileWriter("Mined/"+dataset+"_svm_scores_all"+".csv");
			fr.write("Name\tScore\tUser\tSkyPattern\n");

			for(AIDM_Pattern p : Patterns)
				if(user!=null)
				if(p.dominated==true)
					fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+user.Compute(p)+"\t"+0+"\n");
				else
					fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+user.Compute(p)+"\t"+1+"\n");
				else
					if(p.dominated==true)
						fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+p.getUserRank()+"\t"+0+"\n");
					else
						fr.write(p.getName()+"\t"+p.PredictScoreNorm(weights)+"\t"+p.getUserRank()+"\t"+1+"\n");
					
			
			
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void PrintUserRankedPatterns(String dataset) {
		try {
		    String directoryName = "Mined";
		    File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }

			FileWriter fr = new FileWriter("Mined/"+dataset+".csv");
			fr.write("Name\tUserRank\tAHPRank\n");

			for(AIDM_Pattern p : Patterns)
					fr.write(p.getName()+"\t"+p.getUserRank()+"\n");
			
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void printFeatures(String dataset) {
		try {
		   String directoryName = "FeatureStats";
		    File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }

			
			for(AIDM_Measure m : Patterns.get(0).getMeasures()) {
			 FileWriter fr = new FileWriter(directory+"/"+dataset+"_"+m.getName()+".csv");
			 fr.write(m.getName()+"\n");
			
			for(AIDM_Pattern p : Patterns)
					fr.write(p.getMeasureValue(m.getName())+"\n");
			fr.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		}	
	
}
