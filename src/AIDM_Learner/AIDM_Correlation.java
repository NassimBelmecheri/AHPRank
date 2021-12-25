package AIDM_Learner;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import AIDM_Measures.AIDM_Measure;
import AIDM_Query.AIDM_Query;

public class AIDM_Correlation implements AIDM_ICorrelation {

	@Override
	public double KendallCorrelation(AIDM_Measure m,AIDM_Query Q1) {
		// TODO Auto-generated method stub
		double[] ranking1 = Q1.getlocalRanking(m);
		double[] ranking2 = Q1.getlocalUserRanking();
		KendallsCorrelation kc = new KendallsCorrelation();
		double tau = kc.correlation(ranking1, ranking2);
		return tau;
	}

	@Override
	public double SpearmanCorrelation(AIDM_Measure m,AIDM_Query Q1) {
		// TODO Auto-generated method stub
				double[] ranking1 = Q1.getlocalRanking(m);
				double[] ranking2 = Q1.getlocalUserRanking();
				SpearmansCorrelation sp = new SpearmansCorrelation();
				double rho = sp.correlation(ranking1, ranking2);
				return rho;
	}

	@Override
	public double KendallW(AIDM_Measure m, AIDM_Query Q1) {
		// TODO Auto-generated method stub
				double[] ranking1 = Q1.getlocalRanking(m);
				double[] ranking2 = Q1.getlocalUserRanking();
				int n = ranking1.length;
				double [] r_i = computeRi(ranking1,ranking2);
				double r_= average(r_i);
				double S = computeS(r_i,r_);
				double w = (12*S)/(4*((Math.pow(n, 3))-n));
				if(w<0)
					System.out.print("");
				return w;
	}

	private double computeS(double[] r_i, double r_) {
		double S = 0.0;
		for(int i=0 ;i< r_i.length;i++) {
			double diff=0.0;
			if(r_i[i]!=0)
				diff=(r_i[i]-r_);
			double power = Math.pow(diff,2);
			S= S + power;
		}
		
		return S;
	}

	private double average(double[] r_i) {
		double sum = 0;
		for(int i=0 ;i< r_i.length;i++) {
			sum+=r_i[i];
		}
		return (sum/r_i.length);
	}

	private double[] computeRi(double[] ranking1, double[] ranking2) {
		double[] r_i = new double[ranking1.length];
		for(int i=0 ;i< r_i.length;i++) {
			if(ranking1[i]<=r_i.length&&ranking2[i]<=r_i.length)
			r_i[i]=ranking1[i]+ranking2[i];
			else
				r_i[i]=0.0;
		}
		return r_i;
	}
	public double KendallW(double[] ranking1, double[] ranking2) {

				int n = ranking1.length;
				double [] r_i = computeRi(ranking1,ranking2);
				double r_= average(r_i);
				double S = computeS(r_i,r_);
				double d =(4*(Math.pow(n, 3))-4*n);
				double w = (12*S)/(d);
				
				return w;
	}

	
	
	
	
}
