package AIDM_Experiments;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import AIDM_Algo_AHPIDM.AIDM_AHPAlgorithm;
import AIDM_Algo_AHPIDM.AIDM_AlgoImplementation;
import AIDM_Algo_AHPIDM.AIDM_Algorithm;
import AIDM_Learner.AIDM_Correlation;
import AIDM_Measures.AIDM_Measure;
import AIDM_Measures.AIDM_Measures;

import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Heuristics;
import AIDM_Query.AIDM_Query;
import AIDM_USERS.AIDM_USERS;
import AIDM_USERS.AIDM_USERTYPE;
import AIDM_Utils.AIDM_TimeCounter;
import RankSVM.RankSVM_AlgorithmPassive;
import RankSVM.RankSVM_IAlgorithm;

public class AIDM_Experiment {
	static ArrayList<AIDM_Pattern> P;
	static ArrayList<AIDM_Pattern> learningP;
	static AIDM_Heuristics h;
	static AIDM_Heuristics h1;
	static double k;
	static double iterations;
	static int querysize;
	static double minlen;
	static AIDM_TimeCounter counterahp;
	static AIDM_TimeCounter countersvm;
	static int oracle_i;
	static double support;
	static String dataset;
	static int nb_runs;
	static long timeout;
	static AIDM_USERS Oracle;
	static AIDM_USERS Oracle1;
	static double C=0.005;
	static AIDM_Algorithm algo;
	static String algoname;

	static boolean kendallw;

	static ArrayList<AIDM_Query> queries;
	static AIDM_AHPAlgorithm algorithmahp;
	static RankSVM_IAlgorithm algorithmsvm;
	static RankSVM_AlgorithmPassive algorithmsvm1;
	static HashMap<String, Double> S;
	static String oraclename;
	static ArrayList<ArrayList<Integer>> orders=new ArrayList<ArrayList<Integer>>();

	public static boolean svmtimeout=false;
	public AIDM_Experiment() {

	}

	public static String getMeasures(AIDM_Measures[] measures) {
		String measures_ = "";
		for (AIDM_Measures m : measures)
			measures_ += m + " ";
		return measures_;
	}

	
	public static void InitS(ArrayList<AIDM_Measure> measures) {
		S = new HashMap<String, Double>();
		for (AIDM_Measure m : measures) {
			for (AIDM_Measure m1 : measures) {
				S.put(m.getName() + "-" + m1.getName(), 0.0);
			}
		}
	}

	public static double[] LaunchAHPAlgorithm(ArrayList<AIDM_Query> queries) {

		ArrayList<AIDM_Measure> measure = P.get(0).getMeasures();

		double[] weights = null;
		AIDM_Query q = new AIDM_Query();
		for (AIDM_Pattern p : P) {

			q.AddPattern(p);
		}
		algorithmahp.setQueries(queries);
		if (algorithmahp.process(timeout)) {
			
					weights = algorithmahp.AHP.ComputeWeights(algorithmahp.A, measure);

				
		} else {

			System.err.println("Timeout");

		}

		return weights;
	}

	public static double[] LaunchSVMAlgorithm(ArrayList<AIDM_Query> queries) {

		double[] finalweights = new double[P.get(0).getMeasures().size()];

		algorithmsvm1.queries = queries;

		if (algorithmsvm1.process(timeout)) {

			finalweights = algorithmsvm1.learner.getWeights(P.get(0).getMeasures().size());
			// System.out.print(countersvm.GetAVGWaitingTime());

		} else {
			svmtimeout=true;
			System.err.println("Timeout");
		}

		return finalweights;
	}

	
	


	private static double[] getAvgRecallMeasures(ArrayList<int[]> evsmeasures) {
		double[] resultavg = new double[evsmeasures.get(0).length];
		for (int[] evs : evsmeasures) {
			for (int j = 0; j < evs.length; j++) {
				resultavg[j] += evs[j];
			}
		}
		for (int j = 0; j < resultavg.length; j++) {
			resultavg[j] = resultavg[j] / (k * evsmeasures.size());
		}
		return resultavg;
	}

	public void setKendallw(boolean kendallw2) {
		this.kendallw = kendallw2;

	}

	public static double[] PrintStatsAHP(AIDM_Query q,int topk) {
		
		AIDM_Evaluation eval = new AIDM_Evaluation();
		double[] evs = eval.Ahp_Rec_k(q, topk);

		return new double[] { round((evs[0] / topk), 3) };

	}

	public static double[] PrintStatsSVM(AIDM_Query q,  int topk) {
		AIDM_Evaluation eval = new AIDM_Evaluation();

		double[] evs = eval.Svm_Rec_k(q, topk);

		return new double[] { round((evs[0] / topk), 3) };
	}

	public static void setP(ArrayList<AIDM_Pattern> p) {
		P = p;
	}

	public static void setLearningP(ArrayList<AIDM_Pattern> learningP) {
		AIDM_Experiment.learningP = learningP;
	}

	public static void setH(AIDM_Heuristics h) {
		AIDM_Experiment.h = h;
	}

	public static void setK(double k2) {
		AIDM_Experiment.k = k2;
	}

	public static void setIterations(double iterations) {
		AIDM_Experiment.iterations = iterations;
	}

	public static void setQuerysize(int querysize) {
		AIDM_Experiment.querysize = querysize;
	}

	public static void setMinlen(double minlen) {
		AIDM_Experiment.minlen = minlen;
	}

	public static void setCounterAhp(AIDM_TimeCounter counter) {
		AIDM_Experiment.counterahp = counter;
	}

	public static void setCounterSvm(AIDM_TimeCounter counter) {
		AIDM_Experiment.countersvm = counter;
	}

	public static void setOracle_i(int oracle_i) {
		AIDM_Experiment.oracle_i = oracle_i;
	}

	public static void setSupport(double support) {
		AIDM_Experiment.support = support;
	}

	public static void setDataset(String dataset) {
		AIDM_Experiment.dataset = dataset;
	}

	public static void setNb_runs(int nb_runs) {
		AIDM_Experiment.nb_runs = nb_runs;
	}

	public static void setTimeout(long timeout) {
		AIDM_Experiment.timeout = timeout;
	}



	public static void setOracle(AIDM_USERS oracle) {
		Oracle = oracle;
	}

	public static void setC(double c) {
		C = c;
	}

	public void setAlgorithm(AIDM_Algorithm algorithm) {
		this.algo = algorithm;

	}

	public void setH1(AIDM_Heuristics h1) {
		this.h1 = h1;

	}

	
	public static double rho_ahp(AIDM_Query q, AIDM_USERS oracle, double[] avgweightsahp) {
		double[] ranking1 = new double[q.Patterns.size()];
		double[] ranking2 = new double[q.Patterns.size()];
		int i = q.Patterns.size() - 1;
		for (AIDM_Pattern p : q.Patterns) {
			ranking1[i] = p.getUserRank();
			ranking2[i] = p.getPredictedRank();
			i--;
		}
		double rho = new SpearmansCorrelation().correlation(ranking1, ranking2);
		return rho;
	}
	
	

	public static double rho_svm(AIDM_Query q, AIDM_USERS oracle, double[] avgweightssvm) {

		double[] ranking1 = new double[q.Patterns.size()];
		double[] ranking2 = new double[q.Patterns.size()];
		int i = q.Patterns.size() - 1;
		for (AIDM_Pattern p : q.Patterns) {
			ranking1[i] = p.getUserRank();
			ranking2[i] = p.getPredictedSVMRank();
			i--;
		}
		AIDM_Correlation cr = new AIDM_Correlation();
		double rho = new SpearmansCorrelation().correlation(ranking1, ranking2);
		return rho;
	}
	


	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public void setOracle1(AIDM_USERS oracle1) {
		this.Oracle1 = oracle1;

	}

	protected static AIDM_Algorithm getAlgo() {
	
	
			return algo.AHPIDMPassive;
		
	}

	
	public void build() {

		algorithmsvm1 = new RankSVM_AlgorithmPassive(learningP);
		algorithmsvm1.setPramaters(Oracle, h, k, iterations, querysize, 0.005, countersvm, timeout);

		algorithmahp = new AIDM_AlgoImplementation(learningP, S);
		algorithmahp.setParameters(Oracle, h, kendallw, k, 0, querysize, counterahp);

	}
	

}
