package AIDM_Experiments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import AIDM_USERS.AIDM_USER_LEX;
import AIDM_USERS.AIDM_USER_RANDOM;
import AIDM_Utils.AIDM_Analysis;
import AIDM_Utils.AIDM_TimeCounter;

public class AIDM_Exp_Measures_Correlation extends AIDM_Experiment {
	static AIDM_Algorithm algo = AIDM_Algorithm.AHPIDMPassive;
	static long timeout;
	static int k = 1000;
	static int iteration = 1;
	static int querysize;
	static double training;
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
	static boolean noise = false;
	static boolean kendallw = true;
	static int significant;
	static int rule_type;
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
		case "hepatitis":
			significant = 500000;
			support = 0.24;
			break;
		case "Mushroom":
			significant = 1500000;
			support = 0.05;
			break;
		case "connect":
			significant = 1000000;
			support = 0.1;
			break;
		case "T10I4D100K":
			significant = 75000;
			support = 0.001;
			break;
		case "T40I10D100K":
			significant = 2000000;
			support = 0.009;
			break;
		case "pumsb":
			significant = 150000;
			support = 0.825;
			break;
		case "retail":
			significant = 2500000;
			support = 0.00009;
			break;
		default:
			significant = 0;
			support = 0;
		}
		training = (significant / Double.parseDouble(args[2]));

		timeout = Long.parseLong(args[2]);
		if (args[1] != null)
			nb_runs = Integer.parseInt(args[1]);

		AIDM_Miner miner = new AIDM_Miner();


		HashMap<AIDM_Pattern,Double> P = miner.GetPatterns(getMeasures(measures), dataset.toString(),
				String.valueOf(support));
		

		System.out.println();
		

		AIDM_Query q1 = new AIDM_Query();
		for (AIDM_Pattern p : P.keySet()) {
			q1.AddPattern(p);
		}
		q1.KeepSignificant(significant);

		
		System.out.println(dataset.toString() + " :: Number of AR :: " + q1.Patterns.size());

		System.out.print(q1.Patterns.size());
		String directoryName = "Weights";
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();

		}
		
		for (int f = 0; f < nb_runs; f++) {
			buildOracle(P);
			
			
				double[] aw =new double[7];
						
				// aw = AHP_IDM.LaunchAHPAlgorithm(queriessvm);

				double[] sw =new double[7];

				
			
			AIDM_Analysis demo;
			q1.UserRanking(OracleTest);

	demo = new AIDM_Analysis(dataset.toString()+"_Measures_Chi2" ,0,querysize, measures, q1.Patterns, OracleTest, q1.Patterns.size(),
					true, false,aw, sw);
		}

	}

	private static double getAVG(ArrayList<Double> values) {
		double sum = 0.0;
		for (double v : values)
			sum += v;
		return round(sum / values.size(), 3);
	}
	public static void buildOracle(HashMap<AIDM_Pattern,Double> patternVectors) {

		Oracle = new AIDM_USER_CHI(dataset.toString(), support + "",patternVectors);
		OracleTest = new AIDM_USER_CHI(dataset.toString(), support + "",patternVectors);
		
	}
}
