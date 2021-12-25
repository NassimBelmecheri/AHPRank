package AIDM_Algo_AHPIDM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import AIDM_Learner.AIDM_AHPPassive;
import AIDM_Learner.AIDM_Matrix;
import AIDM_Measures.AIDM_Measure;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Query;
import AIDM_Query.AIDM_SelectionStrategy;

public class AIDM_AlgoImplementation extends AIDM_AHPAlgorithm {
	HashMap<String,Double>S = new HashMap<String,Double>();

	public AIDM_AlgoImplementation(ArrayList<AIDM_Pattern> P,HashMap<String,Double>S) {
		this.P = P;
		this.H = new ArrayList<String[]>();
		setNb_measures();
		A = new AIDM_Matrix();
		InitMatrix(A, P.get(0).getMeasures());
		AHP = new AIDM_AHPPassive(A,S);
		this.S=S;
	}

	@Override
	public boolean process(double timeout) {
		int i = 0;
		ArrayList<AIDM_Measure> measures = P.get(0).getMeasures();
		double[] W_f = new double[measures.size()];
		long elapsed = 0;
		
		long startTime = System.currentTimeMillis();
		 
		
			AHP.AHPPreferenceEstimationIncremental(queries.get(queries.size()-1),l);
		
		long endTime = System.currentTimeMillis();
		elapsed = endTime - startTime;
		counter.Waiting_Time(elapsed);
		l++;
		if (endTime - startTime > timeout) {
			System.err.println(endTime - startTime);
			return false;
		}
		
		
		return true;
	}

	@Override
	public boolean process_new(double timeout) {
		// TODO Auto-generated method stub
		return false;
	}

}
