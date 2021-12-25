package AIDM_Experiments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

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
import AIDM_Utils.AIDM_TimeCounter;

public class AIDM_Exp_Passive extends AIDM_Experiment {
	static AIDM_Algorithm algo = AIDM_Algorithm.AHPIDMPassive;
	static long timeout;
	static int k = 1000;
	static int iteration = 1;
	static int querysize;
	static ArrayList<Double> T;
	static double support;
	static double C = 0.005;
	static String dataset;
	static AIDM_Heuristics h = AIDM_Heuristics.Random;
	static AIDM_Heuristics h1 = AIDM_Heuristics.Random;
	static boolean mine = false;
	static int nb_runs = 1;
	static AIDM_USERTYPE Oracletype ;
	static AIDM_USERS Oracle;
	static AIDM_USERS OracleTest;

	static int significant;
	static int rule_type;
	static AIDM_Measures[] measures = new AIDM_Measures[] { AIDM_Measures.yuleY, AIDM_Measures.cosine,
			AIDM_Measures.laplace, AIDM_Measures.leverage, AIDM_Measures.lambda, AIDM_Measures.lift,
			AIDM_Measures.certainty, AIDM_Measures.chiSquared };

	public static void main(String[] args) throws Exception {
		dataset = args[0];
		T=new ArrayList<Double>();
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

		timeout = Long.parseLong(args[2])*1000;
	if (args[1] != null)
		nb_runs = Integer.parseInt(args[1]);

		T.add(significant / 5.0);
		for(double training : T) {

		AIDM_Miner miner = new AIDM_Miner();
		if (mine) {
			miner.LaunchMiner(dataset.toString(), getMeasures(measures), support + "");

		}


		HashMap<AIDM_Pattern,Double> P = miner.GetPatterns(getMeasures(measures), dataset.toString(),
				String.valueOf(support));
		System.out.println();
		buildOracle(P);
		for (int f = 0; f < nb_runs; f++) {


		AIDM_Query q1 = new AIDM_Query();
		q1.Patterns.addAll(P.keySet());
		ArrayList<double[]>PatternVectors=new ArrayList<double[]>();
		for(AIDM_Pattern p : q1.Patterns)
			PatternVectors.add(p.getVectorMeasures());
		Collections.shuffle(q1.Patterns);
		q1.KeepSignificant(significant);
		
		AIDM_Query qvalidation = new AIDM_Query();
		qvalidation = q1.copy();
		
		System.out.println(dataset.toString() + " :: Number of AR :: " + q1.Patterns.size());

		System.out.print(q1.Patterns.size());
		String directoryName = "Weights";
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();

		}

		ArrayList<double[]> ahpweights = new ArrayList<double[]>();
		ArrayList<double[]> svmweights = new ArrayList<double[]>();

		AIDM_SelectionStrategy strategy = new AIDM_SelectionStrategy(Oracle);
			Collections.shuffle(q1.Patterns);

		ArrayList<AIDM_Query> Validations = new ArrayList<AIDM_Query>();

		ArrayList<Integer> K = new ArrayList<Integer>();
		K.add((int) Math.round((q1.Patterns.size() - training) * 0.1));
		K.add((int) Math.round((q1.Patterns.size() - training) * 0.01));
		
		ArrayList<ArrayList<AIDM_Pattern> >Training = new ArrayList<ArrayList<AIDM_Pattern>>();

		for (int l = 0; l < q1.Patterns.size(); l += training) {
			ArrayList<AIDM_Pattern> TrainP = strategy.KfoldTrain(q1.Patterns, l, training);
			Training.add(TrainP);
			ArrayList<AIDM_Pattern> ValidationP = strategy.KfoldValidation(qvalidation.Patterns, TrainP);
			AIDM_Query q = new AIDM_Query();
			q.Patterns.addAll(ValidationP);
			
			Validations.add(q);
			
		}
			double[] aw =null;
			double[] sw =null;
			
			
		
			int l =0;
			int t =0;
			for(ArrayList<AIDM_Pattern>TrainP:Training) {
				ArrayList<AIDM_Query> queriessvm = new ArrayList<AIDM_Query>();
				InitS(q1.Patterns.get(0).getMeasures());

				AIDM_TimeCounter counter = new AIDM_TimeCounter();
				AIDM_TimeCounter counter1 = new AIDM_TimeCounter();

				AIDM_Experiment AHPRank;

				AHPRank = BuildAHP_IDM(TrainP, TrainP, counter, 1.0);

				AIDM_Experiment RankingSVM;

				RankingSVM = BuildRankingSVM(TrainP,TrainP, counter1, 1.0);
				AIDM_Query query;


				int i = 0;
				System.out.println("Running AHP and SVM " + l + " ...");


					query = new AIDM_Query();

					for (int k = 0; k < TrainP.size(); k++) {
						query.AddPattern(TrainP.get(k));
					}
					
						query.UserRankingFinal(Oracle);
					
						queriessvm.add(query);
					

				
				for(int h =0; h< 10;h++)
				 aw = AHPRank.LaunchAHPAlgorithm(queriessvm);

				
					System.out.println("AHP-Time::" + counterahp.GetWaitingTime());

				ahpweights.add(aw);

				if(!svmtimeout) {
				sw= RankingSVM.LaunchSVMAlgorithm(queriessvm);
				svmweights.add(sw);
				}else {
					sw=new double[aw.length];
				svmweights.add(sw);}

				System.out.println("=================================");
				System.out.println("SVM-Time::" + countersvm.GetWaitingTime());
				l++;
			
		
		rule_type = 0;
		

		double rho_svm = 0.0;
		double rho_ahp = 0.0;



		AIDM_Query q = Validations.get(t);
		q.UserRankingFinal(Oracle);
		algorithmahp.PredictedRanking(q, aw);
		algorithmsvm1.PredictedRanking(q, sw);
		rho_ahp=(rho_ahp(q, Oracle, aw));
		rho_svm=(rho_svm(q, Oracle, sw));


		System.out.println("====rho AHP====" + rho_ahp + "================");

		System.out.println("====rho SVM=====" + rho_svm + "================");
		ArrayList<Double> recallsvm = new ArrayList<Double>();
		ArrayList<Double> recallahp = new ArrayList<Double>();
		for (int topk : K) {
				
			


			
					double[] ahpstats;

				ahpstats = PrintStatsAHP(q, topk);


				double[] svmstats;

				svmstats = PrintStatsSVM(q, topk);



				q.PrintTopKPatternsAHP(dataset + "_" + t, topk);
				q.PrintTopKPatternsSVM(dataset + "_" + t, topk);
				i++;
			
			System.out.println("AHP :: " + Arrays.toString(aw));
			System.out.println("SVM :: " + Arrays.toString(sw));

			System.out.println("==============" + topk + "================");
			System.out.println("====AHP====" + ahpstats[0] + "================");

			System.out.println("====SVM=====" + svmstats[0] + "================");

			recallahp.add(ahpstats[0]);
			recallsvm.add(svmstats[0]);
			
		}


		try {
			 directoryName = "Results";
			 directory = new File(directoryName);
			if (!directory.exists()) {
				directory.mkdir();
			}
			FileWriter myWriter;

				myWriter = new FileWriter("Results/" + dataset+"_Chi2_Passive_Results.txt", true);
				myWriter.write(dataset+"fold"+l + "	" + rho_svm + "	" + rho_ahp + "	" + "	" + recallsvm.get(0) + "	"
						+ recallsvm.get(1) + "	" + recallahp.get(0) + "	" + recallahp.get(1) + "	"
						+ countersvm.GetAVGWaitingTime() + "	" + counterahp.GetAVGWaitingTime() + "\n");


			
			myWriter.close();

			
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		t++;
		}

		}	

		}
		
	}

	private static double getAVG(ArrayList<Double> values) {
		double sum = 0.0;
		for (double v : values)
			sum += v;
		return round(sum / values.size(), 3);
	}

	public static AIDM_Experiment BuildAHP_IDM(ArrayList<AIDM_Pattern> P, ArrayList<AIDM_Pattern> P_MNR,
			AIDM_TimeCounter counter, double d) {
		AIDM_Experiment AHP_IDM;

		AHP_IDM = new Experiment_Builder().setAlgorithm(algo).setPatterns(P).setKendall(kendallw)
				.setLearningP(P_MNR).setQuerysize(querysize).setNb_runs(nb_runs).setK(d).setHeuristicAHP(h)
				.setOracleTest(OracleTest).setOracleTrain(Oracle).setTimeout(timeout).setIterations(iteration)
				.setDataset(dataset.toString()).setOracle_i(rule_type).setCounterAhp(counter).build();

		return AHP_IDM;
	}

	public static AIDM_Experiment BuildRankingSVM(ArrayList<AIDM_Pattern> P, ArrayList<AIDM_Pattern> P_MNR,
			AIDM_TimeCounter counter, double k) {
		AIDM_Experiment APLES;

		APLES = new Experiment_Builder().setPatterns(P).setLearningP(P_MNR).setOracleTest(OracleTest)
				.setOracleTrain(Oracle).setK(k).setHeuristicSVM(h1).setTimeout(timeout).setQuerysize(querysize)
				.setNb_runs(nb_runs).setIterations(iteration).setDataset(dataset.toString())
				.setOracle_i(rule_type).setC(C).setCounterSvm(counter).build();

		return APLES;
	}
	public static void buildOracle(HashMap<AIDM_Pattern,Double> patternVectors) {

		Oracle = new AIDM_USER_CHI(dataset.toString(), support + "",patternVectors);
		OracleTest = new AIDM_USER_CHI(dataset.toString(), support + "",patternVectors);
		
	}
}
