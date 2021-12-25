package AIDM_Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import AIDM_Pattern.AIDM_Pattern;
import AIDM_USERS.AIDM_USERS;
import AIDM_Utils.AIDM_TimeCounter;

public class AIDM_SelectionStrategy implements AIDM_Strategies {

	AIDM_USERS oracle;
	double mu;
	public ArrayList<AIDM_Query> H;
	public ArrayList<double[]> GlobalWeights;
	public HashMap <AIDM_Pattern,Double> Density;
	long timeout;
	AIDM_TimeCounter counter;
	public AIDM_SelectionStrategy(AIDM_USERS oracle) {
		this.mu = mu;
		this.oracle = oracle;
		this.H=new ArrayList<AIDM_Query>();
		this.GlobalWeights=new ArrayList<double[]>();
	
	}
	public AIDM_SelectionStrategy(AIDM_USERS oracle,long timeout) {
		this.mu = mu;
		this.oracle = oracle;
		this.H=new ArrayList<AIDM_Query>();
	this.timeout=timeout;
	this.GlobalWeights=new ArrayList<double[]>();

	}
	
	public void setCounter(AIDM_TimeCounter counter) {
		this.counter = counter;
	}



	

	
	@Override
	public ArrayList<AIDM_Pattern> RandomUniform(ArrayList<AIDM_Pattern> P, int querysize) {
		Collections.shuffle(P);
		ArrayList<AIDM_Pattern> pairs = new ArrayList<AIDM_Pattern>(); 
		pairs.add(P.get(0));
		pairs.add(P.get(1));

		
		return pairs;
	}

	
	
	public ArrayList<AIDM_Pattern> SelectCandidate(AIDM_Heuristics h, ArrayList<AIDM_Pattern> P, double[] weights, int querysize,double k, ArrayList<String[]> H) {
		
		switch (h.toString()) {

		case "Random":
			return RandomUniform(P, querysize);
		}
		return null;
	}

	public ArrayList<AIDM_Pattern> KfoldTrain(ArrayList<AIDM_Pattern> P, int begin,double end) {
		ArrayList<AIDM_Pattern> T=new ArrayList<AIDM_Pattern>();
		double x =begin+end;
		if((begin+end)>P.size())
			x=P.size();
		for(int i = begin; i<x;i++) {
			T.add(P.get(i));
			
			
			}
		return T;
	}
	
	public ArrayList<AIDM_Pattern> KfoldValidation(ArrayList<AIDM_Pattern> P, ArrayList<AIDM_Pattern> TrainP) {
		ArrayList<AIDM_Pattern> T=new ArrayList<AIDM_Pattern>();
		
		
		P.removeAll(TrainP);
		
		return P;
	}

	public ArrayList<AIDM_Pattern> SampleTrain(ArrayList<AIDM_Pattern> P, double training) {
		Collections.shuffle(P);
		ArrayList<AIDM_Pattern> train = new ArrayList<AIDM_Pattern>(); 
		double size =(P.size())*(training/100);
		for(int i =0;i<size;i++) {
			train.add(P.get(i));
		}
		return train;
	}

	public ArrayList<AIDM_Pattern> SampleTest(ArrayList<AIDM_Pattern> P, ArrayList<AIDM_Pattern> TrainP) {
		ArrayList<AIDM_Pattern> T=new ArrayList<AIDM_Pattern>();
		for(int i = 0; i<P.size();i++) {
			if(!TrainP.contains(P.get(i))) {
			T.add(P.get(i));
			}
			
			}
		return T;
	}
	
	private boolean inHistory(ArrayList<double[]> gw) {
		
			double sum =0.0;
			if(gw.size()<2)
				return false;
			for (int i = 0; i < gw.get(0).length; i++) {
				double diff = gw.get(gw.size()-2)[i] - gw.get(gw.size()-1)[i];
				sum += diff;			
			}
				if(sum<=0.05)
					return true;
		

		return false;

	}
	static int binarySearch(String[] arr, String x)
    {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
 
            int res = x.compareTo(arr[m]);
 
            // Check if x is present at mid
            if (res == 0)
                return m;
 
            // If x greater, ignore left half
            if (res > 0)
                l = m + 1;
 
            // If x is smaller, ignore right half
            else
                r = m - 1;
        }
 
        return -1;
    }
}
