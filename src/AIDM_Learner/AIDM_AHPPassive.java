package AIDM_Learner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealVector;

import AIDM_Measures.AIDM_Measure;
import AIDM_Query.AIDM_Query;
import AIDM_Utils.AIDM_Utils;

public class AIDM_AHPPassive implements AIDM_IAHP {
	private AIDM_Matrix A;
	private int[][] score;
	public static double CR;
	ArrayList<AIDM_Matrix> MA =new ArrayList<AIDM_Matrix>();
	HashMap<String,Double>S;
	double rmin;
	double rmax;
	public AIDM_AHPPassive(AIDM_Matrix A,HashMap<String,Double>S) {
		this.A = A;
		this.S=S;
	}
	

	
	@Override
	public double UtilityFunction(double x,double l, double length) {
		if (x>0) {
			double m =0.0;
				if(l==1)
				m = Math.round((length*9* x)+1);
				else
					m = Math.round((l*9* x)+1);

			double scale1=m ;
			if(rmax==rmin)
				scale1= ((m-1)/(m+1))*((9-1)+1);
			else if(rmax>rmin)
				scale1 = ((m-rmin)/(rmax-rmin))*((9-1)+1);

			if(m>9) {
				double scale=0.0;
				if(rmax==rmin)
					scale= ((m-1)/(m+1))*((9-1)+1);
				else if(rmax>rmin)
				scale = ((m-rmin)/(rmax-rmin))*((9-1)+1);

				return Math.round(scale) ;
				
			}
			
			return Math.round(scale1);
		}
		else {
			return 1;

		}
	}


	public  double[] ComputeWeights( AIDM_Matrix A,ArrayList<AIDM_Measure> measures) {
		int size =measures.size();
		double[] weights = new double[size];
		
	     Array2DRowRealMatrix mtx=new Array2DRowRealMatrix(size,size);
	    		 for (int row = 0; row < measures.size(); row++) {
	    			  String m1 = measures.get(row).getName();
	    	            for (int col = 0; col < measures.size(); col++) {
	  	    			  String m2 = measures.get(col).getName();
	    	                mtx.setEntry(row, col, A.Matrix.get(m1+"-"+m2));
	    	            }
	    	        }
	  	       int evIdx = 0 ;

	    try {
	    EigenDecomposition evd = new EigenDecomposition(mtx);
	   
	        for (int i = 0; i < evd.getRealEigenvalues().length; i++) {
	            evIdx = (evd.getRealEigenvalue(i) > evd.getRealEigenvalue(evIdx)) ? i : evIdx;
	        }
	        double sum = 0.0;
	        RealVector v = evd.getEigenvector(evIdx);
	        for (double d : v.toArray()) {
	            sum += d;
	        }
	        for (int k = 0; k < v.getDimension(); k++) {
	            weights[k] = v.getEntry(k) / sum;
	        }
	    } catch(Exception r) {
	    	
	    	System.out.print("error:: "+r);
	    		
        }
		return weights;
	}
	

	@Override
	public  double CR(double lambda,int n) {
		double[] RI= {0,0,0.58,0.9,1.12,1.24,1.32,1.41,1.45,1.49};
		double CI = (lambda-n)/(n-1);
		return CI/RI[n-1];
	}
	public static void InitMatrix(double[][] A) {
		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A.length; j++)
				A[i][j] = 1;
	}

	@Override
	public void AHPPreferenceEstimationIncremental(AIDM_Query query,double l) {
		double sum=l+1.0;
		double f =(l/sum);
		double f1 =(1.0/sum);
		HashMap<String, Double> K = new HashMap<String, Double>();
		AIDM_Correlation coef = new AIDM_Correlation();
		ArrayList<AIDM_Measure> measures = query.getMeasures();
		for (int i =0 ;i<measures.size();i++) {
			query.Rank(measures.get(i).getName());
			double K1 = coef.KendallW(measures.get(i), query);
			if(i==4)
				System.out.print("");
			for (int j =0 ;j<measures.size();j++) {

				query.Rank(measures.get(j).getName());

			double K2 = coef.KendallW(measures.get(j), query);
			K.put(measures.get(i).getName()+"-"+measures.get(j).getName(), (K1-K2));
			}

		}
		
		for(String pair : K.keySet()) {
			double avg = S.get(pair);
			
			double delta = (f*avg)+(f1*K.get(pair));
			S.put(pair,delta );
		}
		double[] minmax=getMaxMin(S,l);
		rmin=minmax[1];
		rmax=minmax[0];

		for (String m : S.keySet()) {
			String[] me = m.split("-");

				if (S.get(m)>0) {

					double v =UtilityFunction(Math.abs(S.get(m)),l,query.Patterns.size());
					if(v ==0)
						v=1.0;
					A.setValue(v, me[0], me[1]);
					double inv =(1 / v);

					A.setValue(inv, me[1], me[0]);
							
				}else {
					double v =UtilityFunction(Math.abs(S.get(m)),l,query.Patterns.size());
					if(v ==0)
						v=1.0;
					A.setValue(v, me[1],me[0]);
					double inv =(1 / v);

					A.setValue(inv, me[0], me[1]);
				}
			}
		
		}



	private double[] getMaxMin(HashMap<String, Double> S,double l) {
		ArrayList<Double> values = new ArrayList<Double>();
		for(double v : S.values()) {
			double m =Math.round((l* Math.abs(v))+1);

			values.add(m);
		}
		
		return new double[] {Collections.max(values),Collections.min(values)};
	}




	

}
