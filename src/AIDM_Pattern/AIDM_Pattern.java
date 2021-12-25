package AIDM_Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;


import AIDM_Measures.AIDM_Measure;

public class AIDM_Pattern implements Comparable<AIDM_Pattern>{

	public String name;
	public String shortname=null;
	public ArrayList<AIDM_Measure> measures;
	public ArrayList<AIDM_Measure> normalizedmeasures;
	public boolean dominated;

	public HashMap<String, Integer> ranking;

	public AIDM_Pattern(String name, ArrayList<AIDM_Measure> measures) {
		this.name = name;
		this.measures = measures;
		this.normalizedmeasures = new ArrayList<AIDM_Measure>();
		init();
		this.ranking = new HashMap<String, Integer>();
		this.dominated=false;
	}

	private void init() {
		for(AIDM_Measure m : measures) {
			normalizedmeasures.add(new AIDM_Measure(m.getName(), m.getValue()));
			
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public ArrayList<AIDM_Measure> getMeasures() {
		return measures;
	}

	public void setMeasures(ArrayList<AIDM_Measure> measures) {
		this.measures = measures;
	}

	@Override
	public String toString() {
		String print =getShortname()+"\n";
		for(AIDM_Measure m :getMeasures())
			print+=m.getName()+" ";
		print+="\n";
		print+=Arrays.toString(getVectorMeasures())+"\n";
		print+="User ranking ::"+ranking.get("oracle");
		return print;
	}

	public double[] getVectorMeasures() {
		double[] vector = new double[measures.size()];
		int i = 0;
		for (AIDM_Measure m : measures) {
			vector[i] = m.getValue();
			i++;
		}
		return vector;
	}
	
	public double[] getNormalizedVectorMeasures() {
		double[] vector = new double[normalizedmeasures.size()];
		int i = 0;
		for (AIDM_Measure m : normalizedmeasures) {
			vector[i] = m.getValue();
			i++;
		}
		return vector;
	}
	
	public double getAVGVectorMeasures() {
		
		double sum = 0.0;
		for (AIDM_Measure m : measures) {
			sum += m.getValue();

		}
		
		return (sum/measures.size());
	}
	public void setRank(String m, int v) {
		ranking.put(m, v);

	}

	public int getRank(AIDM_Measure m) {
		try {
			return ranking.get(m.getName());

		} catch (Exception e) {
			System.err.println("not found");
		}
		return 0;

	}
	public int getRank(String m) {
		try {
			return ranking.get(m);

		} catch (Exception e) {
			System.err.println("not found");
		}
		return 0;

	}
	public int getRankByMeasureName(String m) {
		try {
			for (String m_ : ranking.keySet())
				if(m_.contains(m))
					return ranking.get(m_);

		} catch (Exception e) {
			System.err.println("not found");
		}
		return 0;

	}
	public int getUserRank() {

		try {
					return ranking.get("oracle");

		} catch (Exception e) {
			
			System.err.println("not found oracle for :: "+this.getShortname());
		}
		return 0;
	}
	
	public int getPredictedSVMRank() {

		try {

					return ranking.get("svm");

		} catch (Exception e) {
			System.err.println("not found svm");
		}
		return 0;
	}
	public int getPredictedRank() {

		try {

					return ranking.get("predicted");

		} catch (Exception e) {
			System.err.println("not found");
		}
		return 0;
	}
	public double PredictScore(double[] weights) {
		double score =0.0;
		double[]values =this.getVectorMeasures();
		for(int i =0 ; i<weights.length;i++) {
			score=score+(values[i]*weights[i]);
		}
		return score;
	}
	public double PredictScoreNorm(double[] weights) {
		double score =0.0;
		double[]values =this.getNormalizedVectorMeasures();
		for(int i =0 ; i<weights.length;i++) {
			score=score+(values[i]*weights[i]);
		}
		return score;
	}
	public double getMeasureValue(String string) {
		for (AIDM_Measure m : measures) {
			if (m.getName().equals(string))
				return m.getValue();

		}
		return 0.0;
	}

	public AIDM_Measure getMeasure(String measure) {
		for (AIDM_Measure m : measures) {
			if (m.getName().equals(measure))
				return m;

		}
		return null;
	}
	public AIDM_Measure getNormMeasure(String measure) {
		for (AIDM_Measure m : normalizedmeasures) {
			if (m.getName().equals(measure))
				return m;

		}
		return null;
	}

	@Override
	public int compareTo(AIDM_Pattern pattern) {
	    return -Integer.compare(this.getUserRank(), pattern.getUserRank());
	}
	
	
}
