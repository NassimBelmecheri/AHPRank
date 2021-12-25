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
import AIDM_Measures.AIDM_Measures_SG;
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

	public AIDM_Analysis(String title, int sample, int size, AIDM_Measures_SG[] measures2, ArrayList<AIDM_Pattern> P,
			AIDM_USERS oracle, int n, boolean withmeasures, boolean withfunctions, double[] ahpweights,
			double[] svmweights) {

		ArrayList<HashMap<AIDM_Pattern, Double>> measures = new ArrayList<HashMap<AIDM_Pattern, Double>>();
		HashMap<AIDM_Pattern, Double> chi = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> FAHP = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> FSVM = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> yuleY = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> cosine = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> laplace = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> leverage = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> lambda = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> certainty = new HashMap<AIDM_Pattern, Double>();
		HashMap<AIDM_Pattern, Double> lift = new HashMap<AIDM_Pattern, Double>();

		for (AIDM_Pattern p : P) {
			chi.put(p, oracle.Compute(p));
			FAHP.put(p, p.PredictScore(ahpweights));
			FSVM.put(p, p.PredictScore(svmweights));
			yuleY.put(p, p.getMeasureValue(measures2[0].toString()));
			cosine.put(p, p.getMeasureValue(measures2[1].toString()));
			laplace.put(p, p.getMeasureValue(measures2[2].toString()));
			leverage.put(p, p.getMeasureValue(measures2[3].toString()));
			lambda.put(p, p.getMeasureValue(measures2[4].toString()));
			certainty.put(p, p.getMeasureValue(measures2[6].toString()));
			lift.put(p, p.getMeasureValue(measures2[5].toString()));

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
		HashMap<AIDM_Pattern, Double> FAHP1 = AIDM_Utils.sortByValue(FAHP);
		HashMap<AIDM_Pattern, Double> FSVM1 = AIDM_Utils.sortByValue(FSVM);

		for (AIDM_Pattern p : FAHP1.keySet()) {
			// System.out.println("chi :: "+chi1.get(p));
			// System.out.println("ahp :: "+FAHP1.get(p));
			// System.out.println("svm :: "+FSVM1.get(p));

		}
		if (withmeasures)
			yuleY = AIDM_Utils.sortByValue(yuleY);
		cosine = AIDM_Utils.sortByValue(cosine);
		laplace = AIDM_Utils.sortByValue(laplace);
		leverage = AIDM_Utils.sortByValue(leverage);
		lambda = AIDM_Utils.sortByValue(lambda);
		certainty = AIDM_Utils.sortByValue(certainty);
		lift = AIDM_Utils.sortByValue(lift);

		/*
		 * try { File myObj = new File("results_" + title + "_" + sample + "_" + size +
		 * "chi.csv");
		 * 
		 * FileWriter myWriter = new FileWriter(myObj); if (myObj.createNewFile()) {
		 * System.out.println("File created: " + myObj.getName()); }
		 * 
		 * myWriter.write("Chi\n");
		 * 
		 * 
		 * for (AIDM_Pattern p : chi1.keySet()) { myWriter.write(chi1.get(p)+"	\n");
		 * 
		 * } File myObj1 = new File("results_" + title + "_" + sample + "_" + size +
		 * "AHP.csv");
		 * 
		 * FileWriter myWriter1 = new FileWriter(myObj1); myWriter1.write("AHP\n");
		 * 
		 * for (AIDM_Pattern p : FAHP1.keySet()) {
		 * myWriter1.write(chi1.get(p)+"	\n"); } File myObj2 = new File("results_" +
		 * title + "_" + sample + "_" + size + "SVM.csv");
		 * 
		 * FileWriter myWriter3 = new FileWriter(myObj2); myWriter3.write("SVM \n"); for
		 * (AIDM_Pattern p : FSVM1.keySet()) { myWriter3.write(chi1.get(p)+"	\n");
		 * 
		 * }
		 * 
		 * 
		 * File y = new File("results_" + title + "_" + sample + "_" + size +
		 * "yulesY.csv");
		 * 
		 * FileWriter y_ = new FileWriter(y); y_.write("YuleY \n"); for (AIDM_Pattern p
		 * : yuleY.keySet()) { y_.write(chi1.get(p)+"	\n");
		 * 
		 * } File c = new File("results_" + title + "_" + sample + "_" + size +
		 * "cosine.csv");
		 * 
		 * FileWriter c_ = new FileWriter(c); c_.write("Cosine \n"); for (AIDM_Pattern p
		 * : cosine.keySet()) { c_.write(chi1.get(p)+"	\n");
		 * 
		 * } File la = new File("results_" + title + "_" + sample + "_" + size +
		 * "laplace.csv");
		 * 
		 * FileWriter la_ = new FileWriter(la); la_.write("Laplace \n"); for
		 * (AIDM_Pattern p : laplace.keySet()) { la_.write(chi1.get(p)+"	\n");
		 * 
		 * } File le = new File("results_" + title + "_" + sample + "_" + size +
		 * "leverage.csv");
		 * 
		 * FileWriter le_ = new FileWriter(le); le_.write("Leverage \n"); for
		 * (AIDM_Pattern p : leverage.keySet()) { le_.write(chi1.get(p)+"	\n");
		 * 
		 * } File lam = new File("results_" + title + "_" + sample + "_" + size +
		 * "lambda.csv");
		 * 
		 * FileWriter lam_ = new FileWriter(lam); lam_.write("lambda \n"); for
		 * (AIDM_Pattern p : lambda.keySet()) { lam_.write(chi1.get(p)+"	\n");
		 * 
		 * } File li = new File("results_" + title + "_" + sample + "_" + size +
		 * "lift.csv");
		 * 
		 * FileWriter li_ = new FileWriter(li); li_.write("lift \n"); for (AIDM_Pattern
		 * p : FSVM.keySet()) { li_.write(chi1.get(p)+"	\n");
		 * 
		 * } File cer = new File("results_" + title + "_" + sample + "_" + size +
		 * "certainty.csv");
		 * 
		 * FileWriter cer_ = new FileWriter(cer); cer_.write("Certainty \n"); for
		 * (AIDM_Pattern p : certainty.keySet()) { cer_.write(chi1.get(p)+"	\n");
		 * 
		 * } myWriter.close(); myWriter1.close(); myWriter3.close(); y_.close();
		 * c_.close(); la_.close(); le_.close(); li_.close(); lam_.close();
		 * cer_.close();
		 * 
		 * } catch (IOException e) { System.out.println("An error occurred.");
		 * e.printStackTrace(); }
		 */
		try {

			File f = new File(title + "_Results_Measures.txt");
			if (f.createNewFile()) {
				System.out.println("File created: " + f.getName());
			}

			FileWriter myWriter_ = new FileWriter(f, true);

			for (HashMap<AIDM_Pattern, Double> m : measures) {

				myWriter_.write(CorrelationRho(m, chi1) + "	");

			}

			ArrayList<Integer> K = new ArrayList<Integer>();
			K.add(100);
			K.add(50);
			K.add(10);
			for (AIDM_Measure m : P.get(0).getMeasures()) {
				q.Rank(m.getName());
				for (int k : K) {
					int sum = 0;

					for (AIDM_Pattern s : P) {
						if (s.getRank(m) <= k && s.getUserRank() <= k)
							sum += 1;
					}
					myWriter_.write((sum / k) + "	");

				}
				int sum1 = 0;
			}
			myWriter_.write("\n");

			/*
			 * HashMap<String,Double> measurecoorelations = new HashMap<String,Double> ();
			 * 
			 * for (int i=0;i<measures.size()-1;i++) { for (int j=i+1;j<measures.size();j++)
			 * { measurecoorelations.put(measures2[i].name()+"-"+measures2[j].name(),
			 * CorrelationRho(measures.get(i), measures.get(j))); }
			 * 
			 * } for(String m : measurecoorelations.keySet())
			 * System.out.println(m+" === >"+measurecoorelations.get(m) );
			 */
			myWriter_.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		// System.out.println("measures correlations with oracle : "+coorelations);
		// System.out.println("STD oracle : "+STD(chi));

	}

	private static Double Correlation(HashMap<AIDM_Pattern, Double> m, HashMap<AIDM_Pattern, Double> chi) {
		AIDM_Correlation c = new AIDM_Correlation();
		double[] chi_ = new double[chi.size()];
		double[] m_ = new double[chi.size()];

		int i = 0;
		for (AIDM_Pattern p : chi.keySet()) {
			chi_[i] = chi.get(p);
			m_[i] = m.get(p);
			i++;
		}

		return new KendallsCorrelation().correlation(m_, chi_);
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
