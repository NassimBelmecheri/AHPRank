package AIDM_Miner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AIDM_Parser {
	public AIDM_Parser() {

	}

	public ArrayList<String> GetRules(String dataset, String n) {
		String csvFile = "Mining/" + dataset + "/" + n + "/rules.csv";
		File f = new File(csvFile);
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\t";
		ArrayList<String> rules = new ArrayList<String>();
		try {

			br = new BufferedReader(new FileReader(csvFile));
			int i = 1;
			while ((line = br.readLine()) != null) {
				if (i == 1) {
					i++;

					continue;
				}
				String[] Rules = line.split(cvsSplitBy);
				rules.add(Rules[1]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rules;

	}

	public ArrayList<double[]> GetRulesVectors(String dataset, String n) {
		String csvFile = "Mining/" + dataset + "/" + n + "/rulevectors.csv";
		File f = new File(csvFile);
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\t";
		ArrayList<double[]> V = new ArrayList<double[]>();
		try {

			br = new BufferedReader(new FileReader(csvFile));
			int i = 1;
			while ((line = br.readLine()) != null) {
				if (i == 1) {
					i++;
					continue;
				}
				String[] vector = line.split(cvsSplitBy);
				if (String2Double(vector) != null)
					V.add(String2Double(vector));
				}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//RemovePatternsWithSameOracle(V);
		return V;
	}




	public double[] String2Double(String[] str_) {
		int n = str_.length;
		String[] str = Arrays.copyOfRange(str_, 1, n);
		int size = str.length;
		double[] arr = new double[size];
		for (int i = 0; i < size; i++) {
			if (str[i].contentEquals("NA") || str[i].contains("NaN") || str[i].contains("Infinity"))
				arr[i] =0.0;
				else
				arr[i] = Double.parseDouble(str[i]);
		}
		return arr;
	}

	public static int[] removeTheElement(int[] arr, int index) {

// If the array is empty 
// or the index is not in array range 
// return the original array 
		if (arr == null || index < 0 || index >= arr.length) {

			return arr;
		}

// Create another array of size one less 
		int[] anotherArray = new int[arr.length - 1];

// Copy the elements except the index 
// from original array to the other array 
		for (int i = 0, k = 0; i < arr.length; i++) {

// if the index is 
// the removal element index 
			if (i == index) {
				continue;
			}

// if the index is not 
// the removal element index 
			anotherArray[k++] = arr[i];
		}

// return the resultant array 
		return anotherArray;
	}


}
