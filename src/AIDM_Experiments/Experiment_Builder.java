package AIDM_Experiments;

import java.util.ArrayList;

import AIDM_Algo_AHPIDM.AIDM_Algorithm;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Heuristics;
import AIDM_Query.AIDM_Query;
import AIDM_USERS.AIDM_USERS;
import AIDM_Utils.AIDM_TimeCounter;



public class Experiment_Builder  {

	static ArrayList<AIDM_Pattern> P;
	static ArrayList<AIDM_Pattern> learningP;
	static AIDM_Heuristics h;
	static AIDM_Heuristics h1;
	static double k; 
	static double mu;
	static double alpha;
	static double beta;
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
	static double lambda;
	static int sample;
	static AIDM_USERS Oracle;
	static AIDM_USERS Oracle1;
	static boolean noise;
	static double C;
	static AIDM_Algorithm algorithm;
	static boolean kendallw;
	static ArrayList<AIDM_Query> queries;

	public Experiment_Builder() {	
}
	public   Experiment_Builder setPatterns(ArrayList<AIDM_Pattern> p) {
		P = p;
			return this;
}

	public   Experiment_Builder setLearningP(ArrayList<AIDM_Pattern> learningP) {
		Experiment_Builder.learningP = learningP;
			return this;
}

	public   Experiment_Builder setHeuristicAHP(AIDM_Heuristics h) {
		Experiment_Builder.h = h;
			return this;
}
	public   Experiment_Builder setHeuristicSVM(AIDM_Heuristics h1) {
		Experiment_Builder.h1 = h1;
			return this;
}
	public   Experiment_Builder setK(double d) {
		Experiment_Builder.k = d;
			return this;
}

	public   Experiment_Builder setMu(double mu) {
		Experiment_Builder.mu = mu;
			return this;
}

	public   Experiment_Builder setAlpha(double alpha) {
		Experiment_Builder.alpha = alpha;
			return this;
}

	public   Experiment_Builder setBeta(double beta) {
		Experiment_Builder.beta = beta;
			return this;
}

	public   Experiment_Builder setIterations(double iterations) {
		Experiment_Builder.iterations = iterations;
			return this;
}

	public   Experiment_Builder setQuerysize(int querysize) {
		Experiment_Builder.querysize = querysize;
			return this;
}

	public   Experiment_Builder setMinlen(double minlen) {
		Experiment_Builder.minlen = minlen;
		return this;

}

	public   Experiment_Builder setCounterAhp(AIDM_TimeCounter counter) {
		Experiment_Builder.counterahp = counter;
		return this;

}
	public   Experiment_Builder setCounterSvm(AIDM_TimeCounter counter) {
		Experiment_Builder.countersvm = counter;
		return this;

}

	public   Experiment_Builder setOracle_i(int oracle_i) {
		Experiment_Builder.oracle_i = oracle_i;
		return this;

}

	public   Experiment_Builder setSupport(double support) {
		Experiment_Builder.support = support;
		return this;

}

	public   Experiment_Builder setDataset(String dataset) {
		Experiment_Builder.dataset = dataset;
		return this;

}

	public   Experiment_Builder setNb_runs(int nb_runs) {
		Experiment_Builder.nb_runs = nb_runs;
		return this;

}

	public   Experiment_Builder setTimeout(long timeout) {
		Experiment_Builder.timeout = timeout;
			return this;
}

	public   Experiment_Builder setLambda(double lambda) {
		Experiment_Builder.lambda = lambda;
		return this;

}

	public   Experiment_Builder setSample(int sample) {
		Experiment_Builder.sample = sample;
		return this;

}

	public   Experiment_Builder setOracleTrain(AIDM_USERS oracle) {
		Oracle = oracle;
		return this;

}
	public   Experiment_Builder setOracleTest(AIDM_USERS oracle1) {
		Oracle1 = oracle1;
		return this;

}
	public   Experiment_Builder setNoise(boolean noise) {
		Experiment_Builder.noise = noise;
		return this;
}

	public  Experiment_Builder setC(double c) {
		C = c;
		return this;
}

	public  Experiment_Builder setAlgorithm(AIDM_Algorithm algo) {
		algorithm = algo;
		return this;
}
	public Experiment_Builder setKendall(boolean kendallw1) {
		kendallw=kendallw1;
		return this;
	}
	
	public AIDM_Experiment build() {
			
		AIDM_Experiment exp = new AIDM_Experiment();
		exp.setAlgorithm(algorithm);
		exp.setC(C);
		exp.setCounterAhp(counterahp);
		exp.setCounterSvm(countersvm);
		exp.setDataset(dataset);
		exp.setH(h);
		exp.setH1(h1);
		exp.setIterations(iterations);
		exp.setK(k);
		exp.setLearningP(learningP);
		exp.setP(P);
		exp.setMinlen(minlen);
		exp.setNb_runs(nb_runs);
		exp.setOracle(Oracle);
		exp.setOracle1(Oracle1);
		exp.setOracle_i(oracle_i);
		exp.setQuerysize(querysize);
		exp.setSupport(support);
		exp.setTimeout(timeout);
		exp.setKendallw(kendallw);
		exp.build();

		return exp;
}
	
}
