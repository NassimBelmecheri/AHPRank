package RankSVM;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Heuristics;
import AIDM_Query.AIDM_Query;
import AIDM_Query.AIDM_SelectionStrategy;
import AIDM_USERS.AIDM_USERS;
import AIDM_Utils.AIDM_TimeCounter;
import AIDM_Utils.AIDM_Utils;

public class RankSVM_AlgorithmPassive {

	double iterations;
	int querysize;
	double alpha;
	double beta;
	double mu;
	int nb_measures;
	ArrayList<AIDM_Pattern> P;
	AIDM_USERS Oracle;
	AIDM_Heuristics heuristic;
	ArrayList<AIDM_Query> H;
	public RankSVM learner;
	AIDM_TimeCounter counter;
	double k;
	double minlen;
	double lambda;
	int sample ;
	public ArrayList<AIDM_Query> queries;
	int i = 0;
	int qid=1;

	public RankSVM_AlgorithmPassive(ArrayList<AIDM_Pattern> P) {
		this.queries= new ArrayList<AIDM_Query>();
		this.P = P;
		Init();
		
	}

	

	public boolean process(double timeout) {
		double[] W_f = new double[nb_measures];
		
		long elapsed = 0;
			try {
				for(AIDM_Query query : queries) {
					
					learner.BuildDataset(query,qid);
					qid++;
				}
				//System.out.println("Data loaded and labeled");
				long startTime = System.currentTimeMillis();
				learner.learn(i+1);
				long endTime = System.currentTimeMillis();
				elapsed = endTime - startTime;
				W_f = learner.getWeights(W_f.length);
				//System.out.println(Arrays.toString(W_f));
				
				counter.Waiting_Time(elapsed);
				if (endTime - startTime > timeout) {
					System.err.println(endTime - startTime);

					return false;
				}
				i++;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		//System.err.println("Execution Time :: " + Collections.max(counter.times));
		//System.err.println("AVG Waiting Time :: " + counter.GetAVGWaitingTime());

		return true;
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
	public double[] PredictedRankingValue(AIDM_Query query, double[] weights) {
		HashMap<AIDM_Pattern, Double> patterns = new HashMap<AIDM_Pattern, Double>();

		for (AIDM_Pattern p : query.Patterns) {
			double score = p.PredictScore(weights);
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

public void setPramaters(AIDM_USERS oracle, AIDM_Heuristics h,  double k2,
		double iterations, int querysize, double C,
		AIDM_TimeCounter counter,long timeout) {
	this.Oracle = oracle;
	this.heuristic = h;
	this.iterations = iterations;
	this.querysize = querysize - 1;
	this.learner = new RankSVM(C,h.toString(),iterations,timeout);
	this.counter = counter;
	this.k=k2;

	this.H=new ArrayList<AIDM_Query>();
}
}
