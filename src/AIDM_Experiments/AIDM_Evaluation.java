package AIDM_Experiments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import AIDM_Learner.AIDM_Correlation;
import AIDM_Measures.AIDM_Measure;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Query;
import AIDM_USERS.AIDM_USERS;
import AIDM_USERS.AIDM_USER_CHI;

public class AIDM_Evaluation {

	public double[] Ahp_Rec_k(AIDM_Query q, int k) {
		double sum =0;
		for (AIDM_Pattern p : q.Patterns) {
			
			
			if (p.ranking.get("oracle") <= k &&  p.ranking.get("predicted")<=k) {
				sum++;
			}
			
		}

		return new double[] { sum };
	}
	public double[] Svm_Rec_k(AIDM_Query q, int k) {
		double sum =0;
		for (AIDM_Pattern p : q.Patterns) {
			
			if (p.ranking.get("oracle") <= k &&  p.ranking.get("svm")<=k) {
				sum++;
			}
			
		}

		return new double[] { sum  };
	}


	public double rho(HashMap<String, Integer> model, HashMap<String, Integer> oracle, int k) {

		double[] ranking1 = new double[k];
		double[] ranking2 = new double[k];
		int j = 0;
		for (String s : model.keySet()) {
			ranking1[j] = model.get(s);
			ranking2[j] = oracle.get(s);
			j++;

		}

		SpearmansCorrelation sp = new SpearmansCorrelation();
		double rho = 0.0;
		rho = sp.correlation(ranking1, ranking2);

		return rho;

	}







}
