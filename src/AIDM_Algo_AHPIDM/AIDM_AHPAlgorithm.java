package AIDM_Algo_AHPIDM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import AIDM_Learner.AIDM_AHPPassive;
import AIDM_Learner.AIDM_Matrix;
import AIDM_Measures.AIDM_Measure;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Heuristics;
import AIDM_Query.AIDM_Query;
import AIDM_USERS.AIDM_USERS;
import AIDM_Utils.AIDM_TimeCounter;
import AIDM_Utils.AIDM_Utils;

abstract public class AIDM_AHPAlgorithm {
	double criterion;

	int querysize;
	double alpha;
	double beta;
	double mu;
	public AIDM_AHPPassive AHP;
	public static ArrayList<AIDM_Query> queries;

	public AIDM_Matrix A;
	int nb_measures;
	ArrayList<AIDM_Pattern> P;
	AIDM_USERS Oracle;
	AIDM_Heuristics heuristic;
	ArrayList<String[]> H;
	AIDM_TimeCounter counter;
	double l=1;
	double minlen;
	double lambda;
	boolean kendallw;
	public static boolean once = false;
	
	public abstract boolean process(double timeout);
	public abstract boolean process_new(double timeout);

	public void setParameters(AIDM_USERS oracle, AIDM_Heuristics h,boolean kendallw, double k,
			double iterations, int querysize,   AIDM_TimeCounter counter) {
		this.Oracle = oracle;
		this.heuristic = h;

		this.criterion = iterations;
		this.querysize = querysize ;
		this.counter = counter;
		this.l = k;


		this.kendallw=kendallw;
	}

	public AIDM_Query UserRanking(AIDM_Query query) {
		AIDM_Query R = query.copy();
			R.UserRanking(Oracle);

		return R;
	}

	public void PredictedRanking(AIDM_Query query, double[] weights) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();

		for (AIDM_Pattern p : query.Patterns) {
			double score = p.PredictScoreNorm(weights);
			patterns.put(p, score);
		}

		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(patterns);

		int i = 1;
		for (AIDM_Pattern p : sorted.keySet()) {

			p.setRank("predicted", i);
			i++;

		}
	}
	
	public double[] PredictedRankingValue(AIDM_Query query, double[] weights) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();

		for (AIDM_Pattern p : query.Patterns) {
			double score = p.PredictScoreNorm(weights);
			patterns.put(p, score);
		}

		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(patterns);

		double[] values = new double[sorted.size()];
		int i =0;
		for (AIDM_Pattern p : sorted.keySet()) {

			values[i]=sorted.get(p);
			i++;

		}
		return values;
	}

	public static void InitMatrix(AIDM_Matrix A, ArrayList<AIDM_Measure> measure) {
		for (AIDM_Measure m : measure)
			for (AIDM_Measure m1 : measure)
				A.setValue(1, m.getName(), m1.getName());
	}

	public double getIterations() {
		return criterion;
	}

	public void setIterations(int iterations) {
		this.criterion = iterations;
	}

	public int getQuerysize() {
		return querysize;
	}

	public void setQuerysize(int querysize) {
		this.querysize = querysize;
	}

	public int getNb_measures() {
		return nb_measures;
	}

	public void setNb_measures() {
		this.nb_measures = P.get(0).getMeasures().size();
	}

	public AIDM_USERS getOracle() {
		return Oracle;
	}

	public void setOracle(AIDM_USERS oracle) {
		Oracle = oracle;
	}

	public void reset() {
		for (AIDM_Pattern p : this.P)
			p.ranking = new HashMap<>();

	}

	public void setQueries(ArrayList<AIDM_Query> queries) {
		this.queries=queries;
	}
}
