package AIDM_Learner;

import java.util.ArrayList;

import AIDM_Query.AIDM_Query;

public interface AIDM_IAHP {


public void AHPPreferenceEstimationIncremental(AIDM_Query query,double l);	

public double UtilityFunction(double x,double l,double querysize);

public double CR(double lambda,int n);

}
