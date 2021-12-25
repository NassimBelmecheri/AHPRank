package RankSVM;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Heuristics;
import AIDM_Query.AIDM_Query;
import AIDM_Query.AIDM_SelectionStrategy;
import AIDM_USERS.AIDM_USERS;
import AIDM_Utils.AIDM_TimeCounter;
import AIDM_Utils.AIDM_Utils;

public class RankSVM_IAlgorithm {

	double iterations;
	int querysize;
	double alpha;
	double beta;
	double mu;
	int nb_measures;
	ArrayList<AIDM_Pattern> P;
	AIDM_USERS Oracle;
	AIDM_Heuristics heuristic;
	ArrayList<String[]> H;
	public RankSVM learner;
	AIDM_TimeCounter counter;
	double k;
	double minlen;
	double lambda;
	int sample ;
	private Object timeout;
	public RankSVM_IAlgorithm(ArrayList<AIDM_Pattern> P) {
		this.P = P;
		Init();
		
	}

	

	private AIDM_Query NoiseUserRanking(AIDM_Query query) {
		AIDM_Query R = query.copy();
		R.UserRanking(Oracle);
		R.sortByRank();
		//System.out.println(R.Patterns);
		//System.out.println(Arrays.toString(R.getlocalUserRanking()));
		int i =1;
		for(AIDM_Pattern p : R.Patterns) {
			p.setRank("oracle", i);
			i++;
		}
		return R;
		
	}

	public AIDM_Query UserRanking(AIDM_Query R) {
				R.UserRanking(Oracle);
		return R;
	}

	public void PredictedRanking(AIDM_Query query, double[] weights) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();
		for (AIDM_Pattern p : query.Patterns) {
			double score = p.PredictScore(weights);
			patterns.put(p, score);
		}

		HashMap<AIDM_Pattern, Double> sorted = new AIDM_Utils().sortByValue(patterns);

		int i = 1;
		for (AIDM_Pattern p : sorted.keySet()) {

			p.setRank("svm", i);
			i++;

		}
	}

	public static void InitMatrix(double[][] A) {
		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A.length; j++)
				A[i][j] = 1;
	}

	public double getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
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

	public void Init() {
		setNb_measures();
		File file = new File("SVM/data/train.dat");
		file.delete();

	}
public  double[] GenerateWeights() {
		
		// here i searched and found that a way of generating random weights is to take a negative log of uniformly distributed value.
		  double a[] = new double[nb_measures];
	        double s = 0.0d;
	        Random random = new Random();
	        for (int i = 0; i < nb_measures; i++)
	        {
	           a [i] = 1.0d - random.nextDouble();
	           a [i] = -1 * Math.log(a[i]);
	           s += a[i];
	        }
	      // i normalized it so it would sum up to 1
	        for (int i = 0; i < nb_measures; i++)
	        {
	           a [i] /= s;
	        }
	        return a;
	}

public void setPramaters(AIDM_USERS oracle, AIDM_Heuristics h, double k2,
		double iterations, int querysize, double C,
		AIDM_TimeCounter counter,long timeout) {
	this.Oracle = oracle;
	this.heuristic = h;
	this.iterations = iterations;
	this.querysize = querysize;
	this.learner = new RankSVM(C,h.toString(),iterations,timeout);
	this.counter = counter;
	this.k=k2;
	this.H=new ArrayList<String[]>();
}
}
