package AIDM_Experiments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import AIDM_Algo_AHPIDM.AIDM_Algorithm;
import AIDM_Measures.AIDM_Measures;
import AIDM_Miner.AIDM_Miner;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Heuristics;
import AIDM_Query.AIDM_Query;
import AIDM_Query.AIDM_SelectionStrategy;
import AIDM_USERS.AIDM_USERS;
import AIDM_USERS.AIDM_USERTYPE;
import AIDM_USERS.AIDM_USER_CHI;
import AIDM_USERS.AIDM_USER_LEX;
import AIDM_USERS.AIDM_USER_RANDOM;
import AIDM_Utils.AIDM_TimeCounter;

public class AIDM_EXP_Active extends AIDM_Experiment {
	static AIDM_Algorithm algo = AIDM_Algorithm.AHPIDMPassive;
	static long timeout = 3600000;
	static double iteration;
	static int querysize;
	static double support;
	static double C = 0.005;
	static String dataset;
	static AIDM_Heuristics h;
	static AIDM_Heuristics h1;
	static boolean mine = false;
	static int nb_runs = 1;
	static AIDM_USERTYPE Oracletype = AIDM_USERTYPE.ChiSquare;
	static AIDM_USERS Oracle;
	static AIDM_USERS OracleTest;

	static int significant;
	static int rule_type;
	static ArrayList<String[]> H = new ArrayList<String[]>();
	static ArrayList<String[]> H1 = new ArrayList<String[]>();

	static AIDM_Measures[] measures = new AIDM_Measures[] { AIDM_Measures.yuleY, AIDM_Measures.cosine,
			AIDM_Measures.laplace, AIDM_Measures.leverage, AIDM_Measures.lambda, AIDM_Measures.lift,
			AIDM_Measures.certainty, AIDM_Measures.chiSquared };

	public static void main(String[] args) throws Exception {
		dataset = args[0];
		switch (dataset) {
		case "zoo":
			significant = 500;
			support = 0.3;
			break;
		case "vote":
			significant = 1000;
			support = 0.3;
			break;
		case "anneal":
			significant = 5000;
			support = 0.8;
			break;
		case "chess":
			significant = 10000;
			support = 0.8;
			break;
		case "Mushroom":
			significant = 20000;
			support = 0.2;
			break;
		case "connect":
			significant = 50000;
			support = 0.92;
			break;
		case "T10I4D100K":
			significant = 75000;
			support = 0.001;
			break;
		case "T40I10D100K":
			significant = 100000;
			support = 0.01;
			break;
		case "pumsb":
			significant = 150000;
			support = 0.825;
			break;
		case "retail":
			significant = 200000;
			support = 0.00015;
			break;
		default:
			significant = 0;
			support = 0;
		}
		querysize = 2;
		nb_runs = Integer.parseInt(args[1]);
		timeout = Long.parseLong(args[2]);

	
			h = h1 = AIDM_Heuristics.Random;

		

		AIDM_Miner miner = new AIDM_Miner();
		


		HashMap<AIDM_Pattern,Double> P = miner.GetPatterns(getMeasures(measures), dataset.toString(),
				String.valueOf(support));




		AIDM_Query q = new AIDM_Query();
		for (AIDM_Pattern p : P.keySet()) {
			q.AddPattern(p);
		}
		
		
		q.KeepSignificant(significant);

		AIDM_Query qtest = q.copy();
		buildOracle(P);
		System.out.println(
				dataset.toString() + " :: Number of AR :: " + q.Patterns.size() + " :: Nb Iterations ::" + iteration);

		InitS(q.Patterns.get(0).getMeasures());

		ArrayList<double[]> ahpweights = new ArrayList<double[]>();
		ArrayList<double[]> svmweights = new ArrayList<double[]>();

		AIDM_SelectionStrategy strategy = new AIDM_SelectionStrategy(Oracle, timeout);

		ArrayList<AIDM_Query> queries = new ArrayList<AIDM_Query>();
		ArrayList<Integer> K = new ArrayList<Integer>();
		K.add((int) Math.round((q.Patterns.size()) * 0.1));
		K.add((int) Math.round((q.Patterns.size()) * 0.01));

		double rhoahp = -1.0;
		double rhosvm = -1.0;
		double sigmaahp = 0.0;
		double sigmasvm = 0.0;

		q.UserRankingFinal(Oracle);
		qtest.UserRankingFinal(Oracle);

		double[] UserRanking = qtest.getUserRanking();

		double[] aw = new double[q.Patterns.get(0).getMeasures().size()];
		double[] sw = new double[q.Patterns.get(0).getMeasures().size()];

		HashMap<String, ArrayList<Double>> alliterations = new HashMap<String, ArrayList<Double>>();
		
		try {
			String directoryName = "Results";
			File directory = new File(directoryName);
			if (!directory.exists()) {
				directory.mkdir();

			}
			 directoryName = "Weights";
			 directory = new File(directoryName);
			if (!directory.exists()) {
				directory.mkdir();

			}
			
			FileWriter myWriter = new FileWriter(  "Results/" + dataset + "_Random_Chi2_Active_Results.txt", true);

			AIDM_TimeCounter counter = new AIDM_TimeCounter();
			AIDM_TimeCounter counter1 = new AIDM_TimeCounter();

			AIDM_Experiment AHP_IDM;

			AHP_IDM = BuildAHP_IDM(q.Patterns, counter, 1.0);

			AIDM_Experiment APLES;

			APLES = BuildALPES(q.Patterns, counter1, 1.0);
			long RunningTime =0;

			long startTime = System.currentTimeMillis();

			double stoppingcriterionahp=0;
			double stoppingcriterionsvm=0;
			ArrayList<HashMap<String,Double>> ahp= new ArrayList<HashMap<String,Double>>();
			ArrayList<HashMap<String,Double>>  svm= new ArrayList<HashMap<String,Double>>();
			ArrayList<AIDM_Query> queriesahp = new ArrayList<AIDM_Query>();
			int i=0;
			while (i < nb_runs) {

				ArrayList<Double> recallsvm = new ArrayList<Double>();
				ArrayList<Double> recallahp = new ArrayList<Double>();

				AIDM_Query query1 = new AIDM_Query();

				ArrayList<AIDM_Pattern> pair = strategy.SelectCandidate(AIDM_Heuristics.Random, q.Patterns, aw, querysize, k, H);
				query1.AddPattern(pair.get(0));
				query1.AddPattern(pair.get(1));
				if (!Contains(queries,query1)) {

					query1.UserRanking(Oracle);
					System.out.print(query1);
					queries.add(query1);
				} else {
					continue;
				}
					query1.UserRanking(Oracle);

					queries.add(query1);
				
				aw = AHP_IDM.LaunchAHPAlgorithm(queries);
				if(!svmtimeout) {
				sw = APLES.LaunchSVMAlgorithm(queries);
				}else {
					sw=new double[aw.length];}
			
				
				long endTime = System.currentTimeMillis();
				RunningTime=(endTime-startTime);

				algorithmahp.PredictedRanking(qtest, aw);
				
				double[] function1 = qtest.getPredictionRanking();

				rhoahp = new SpearmansCorrelation().correlation(UserRanking, function1);

				algorithmsvm1.PredictedRanking(qtest, sw);
				double[] function = qtest.getPredictionSVMRanking();

				rhosvm = new SpearmansCorrelation().correlation(UserRanking, function);
				
				for (int topk : K) {

					double[] ahpstats;

					ahpstats = PrintStatsAHP(qtest, topk);

					recallahp.add(ahpstats[0]);

					double[] svmstats;

					svmstats = PrintStatsSVM(qtest, topk);

					recallsvm.add(svmstats[0]);
					
					

				}

				myWriter.write(i+ "\t" + rhosvm + "\t" + rhoahp + "\t" + sigmasvm + "\t" + sigmaahp+ "\t" + recallsvm.get(0) + "\t"
						+ recallsvm.get(1) + "\t" + recallahp.get(0) + "\t" + recallahp.get(1) + "\t"
						+ countersvm.GetAVGWaitingTime() + "\t" + counterahp.GetAVGWaitingTime() + "\t"
						+ countersvm.getGenerationTime() + "\t" + counterahp.getGenerationTime()+ "\t"+stoppingcriterionsvm+ "\t"+ stoppingcriterionahp+ "\n\n");

				System.out.println(i + "\t" + rhosvm + "\t" + rhoahp+ "\t" + sigmasvm + "\t" + sigmaahp + "\t" + recallsvm.get(0) + "\t"
						+ recallsvm.get(1) + "\t" + recallahp.get(0) + "\t" + recallahp.get(1) + "\t"
						+ countersvm.GetAVGWaitingTime() + "\t" + counterahp.GetAVGWaitingTime() + "\t"
						+ countersvm.getGenerationTime() + "\t" + counterahp.getGenerationTime() + "\t"+stoppingcriterionsvm+ "\t"+ stoppingcriterionahp+ "\n\n");
				i++;

			}

	

			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			// System.outprintln("An error occurred.");
			e.printStackTrace();
		}

		
	}

	
	private static boolean Contains(ArrayList<AIDM_Query> queries, AIDM_Query query) {
		for (AIDM_Query q : queries) {
			for (AIDM_Pattern p : query.Patterns)
				if (q.Patterns.contains(p))
					return true;
		}
		return false;
	}

	public static AIDM_Experiment BuildAHP_IDM(ArrayList<AIDM_Pattern> P, AIDM_TimeCounter counter, double d) {
		AIDM_Experiment AHP_IDM;

		AHP_IDM = new Experiment_Builder().setAlgorithm(algo).setPatterns(P).setKendall(kendallw).setLearningP(P)
				.setQuerysize(querysize).setNb_runs(nb_runs).setK(d).setHeuristicAHP(h).setOracleTest(OracleTest)
				.setOracleTrain(Oracle).setTimeout(timeout).setIterations(iteration).setDataset(dataset.toString())
				.setOracle_i(rule_type).setCounterAhp(counter).build();

		return AHP_IDM;
	}

	public static AIDM_Experiment BuildALPES(ArrayList<AIDM_Pattern> P, AIDM_TimeCounter counter, double d) {
		AIDM_Experiment APLES;

		APLES = new Experiment_Builder().setPatterns(P).setLearningP(P).setOracleTest(OracleTest).setOracleTrain(Oracle)
				.setK(d).setHeuristicSVM(h1).setTimeout(timeout).setQuerysize(querysize).setNb_runs(nb_runs)
				.setIterations(iteration).setDataset(dataset.toString()).setOracle_i(rule_type)
				.setC(C).setCounterSvm(counter).build();

		return APLES;
	}
	public static void buildOracle(HashMap<AIDM_Pattern,Double> patternVectors) {

		Oracle = new AIDM_USER_CHI(dataset.toString(), support + "",patternVectors);
		OracleTest = new AIDM_USER_CHI(dataset.toString(), support + "",patternVectors);
		
	}
}
