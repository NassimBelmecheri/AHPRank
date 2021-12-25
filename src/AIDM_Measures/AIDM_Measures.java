package AIDM_Measures;

import java.util.EnumSet;

public enum AIDM_Measures {
	addedValue, collectiveStrength, cosine, laplace, support, gini, phi, kappa, jaccard, yuleQ, yuleY, oddsRatio,
	mutualInformation, klosgen, lambda, certainty, lift, leverage,Oracle, chiSquared, fishersExactTest;

	public static EnumSet<AIDM_Measures> group1 = EnumSet.of(oddsRatio, yuleQ, yuleY);
	public static EnumSet<AIDM_Measures> group2 = EnumSet.of(cosine, jaccard);
	public static EnumSet<AIDM_Measures> group3 = EnumSet.of(support, laplace);
	public static EnumSet<AIDM_Measures> group4 = EnumSet.of(phi, collectiveStrength, leverage);
	public static EnumSet<AIDM_Measures> group5 = EnumSet.of(gini, lambda);
	public static EnumSet<AIDM_Measures> group6 = EnumSet.of(lift, addedValue, klosgen);
	public static EnumSet<AIDM_Measures> group7 = EnumSet.of(mutualInformation, certainty, kappa);
}
