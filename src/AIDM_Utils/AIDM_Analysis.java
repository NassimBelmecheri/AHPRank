package AIDM_Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import AIDM_Learner.AIDM_Correlation;
import AIDM_Measures.AIDM_Measure;
import AIDM_Measures.AIDM_Measures;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Query;
import AIDM_USERS.AIDM_USERS;

public class AIDM_Analysis {

	public AIDM_Analysis(String title, int sample, int size, AIDM_Measures[] measures2, ArrayList<AIDM_Pattern> P,
			AIDM_USERS oracle, int n, boolean withmeasures, boolean withfunctions, double[] ahpweights,
			double[] svmweights) {

		ArrayList<HashMap<AIDM_Pattern, Double>> measures = new ArrayList<HashMap<AIDM_Pattern, Double>>();
		HashMap<AIDM_Pattern, Double> chi = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> yuleY = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> cosine = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> laplace = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> leverage = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> lambda = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> lift = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> certainty = new HashMap<AIDM_Pattern, Double>();

		for (AIDM_Pattern p : P) {
			chi.put(p, oracle.Compute(p));
			yuleY.put(p, p.getMeasureValue(measures2[0].toString()));
			cosine.put(p, p.getMeasureValue(measures2[1].toString()));
			laplace.put(p, p.getMeasureValue(measures2[2].toString()));
			leverage.put(p, p.getMeasureValue(measures2[3].toString()));
			lambda.put(p, p.getMeasureValue(measures2[4].toString()));
			lift.put(p, p.getMeasureValue(measures2[5].toString()));
			certainty.put(p, p.getMeasureValue(measures2[6].toString()));

		}
		AIDM_Query q = new AIDM_Query();
		for (AIDM_Pattern p : P)
			q.AddPattern(p);
		// Normalize(FSVM);

		for (AIDM_Measure m : P.get(0).getMeasures()) {
			HashMap<AIDM_Pattern, Double> measure = new HashMap<AIDM_Pattern, Double>();
			for (AIDM_Pattern p : P) {

				measure.put(p, p.getMeasureValue(m.getName()));

				if (measures.size() == n)
					break;
			}

			measures.add(measure);
		}
		ArrayList<Double> coorelations = new ArrayList<Double>();

		HashMap<AIDM_Pattern, Double> chi1 = AIDM_Utils.sortByValue(chi);

	
		if (withmeasures)
			yuleY = AIDM_Utils.sortByValue(yuleY);
		cosine = AIDM_Utils.sortByValue(cosine);
		laplace = AIDM_Utils.sortByValue(laplace);
		leverage = AIDM_Utils.sortByValue(leverage);
		lambda = AIDM_Utils.sortByValue(lambda);
		lift = AIDM_Utils.sortByValue(lift);
		certainty = AIDM_Utils.sortByValue(certainty);

		try {

			File f = new File(title + "_Results_Measures.txt");
			if (f.createNewFile()) {
				System.out.println("File created: " + f.getName());
			}

			FileWriter myWriter_ = new FileWriter(f, true);

			for (HashMap<AIDM_Pattern, Double> m : measures) {

				myWriter_.write(CorrelationRho(m, chi1) + "	");

			}

			
			myWriter_.write("\n");

			
			myWriter_.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// System.out.println("measures correlations with oracle : "+coorelations);
		// System.out.println("STD oracle : "+STD(chi));

	}

	private static Double CorrelationRho(HashMap<AIDM_Pattern, Double> m, HashMap<AIDM_Pattern, Double> chi) {
		AIDM_Correlation c = new AIDM_Correlation();
		double[] chi_ = new double[chi.size()];
		double[] m_ = new double[chi.size()];

		int i = 0;
		for (AIDM_Pattern p : chi.keySet()) {
			chi_[i] = chi.get(p);
			m_[i] = m.get(p);
			i++;
		}
		return new SpearmansCorrelation().correlation(m_, chi_);
	}

	private static Double STD(HashMap<AIDM_Pattern, Double> chi) {
		AIDM_Correlation c = new AIDM_Correlation();
		double[] chi_ = new double[chi.size()];

		int i = 0;
		for (AIDM_Pattern p : chi.keySet()) {
			chi_[i] = chi.get(p);
			i++;
		}

		StandardDeviation sd = new StandardDeviation(false);

		return sd.evaluate(chi_);
	}

	private static Double Variance(HashMap<AIDM_Pattern, Double> m) {
		AIDM_Correlation c = new AIDM_Correlation();
		double min = Collections.min(m.values());

		double max = Collections.max(m.values());

		return (max - min);
	}

	public static void Normalize(HashMap<AIDM_Pattern, Double> FSVM) {
		ArrayList<Double> values = new ArrayList(FSVM.values());
		double min = Collections.min(values);
		double max = Collections.max(values);
		for (AIDM_Pattern p : FSVM.keySet()) {
			double x = FSVM.get(p);
			double norm = (x - min) / (max - min);
			FSVM.put(p, norm);
		}

	}

}
