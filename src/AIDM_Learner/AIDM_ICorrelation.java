package AIDM_Learner;

import AIDM_Measures.AIDM_Measure;
import AIDM_Query.AIDM_Query;

public interface AIDM_ICorrelation {
 public double SpearmanCorrelation(AIDM_Measure m,AIDM_Query Q1);
 public double KendallCorrelation(AIDM_Measure m,AIDM_Query Q1);
 public double KendallW(AIDM_Measure m,AIDM_Query Q1);

	
}
