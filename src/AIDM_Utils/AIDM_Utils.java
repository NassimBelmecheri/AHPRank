package AIDM_Utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import javax.swing.BorderFactory;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

import AIDM_Measures.AIDM_Measure;
import AIDM_Pattern.AIDM_Pattern;
import AIDM_USERS.AIDM_USERS;

public class AIDM_Utils {

	public static HashMap<AIDM_Pattern, Double> sortByValue(HashMap<AIDM_Pattern, Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<AIDM_Pattern, Double>> list = new LinkedList<Map.Entry<AIDM_Pattern, Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<AIDM_Pattern, Double>>() {
			public int compare(Map.Entry<AIDM_Pattern, Double> o1, Map.Entry<AIDM_Pattern, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<AIDM_Pattern, Double> temp = new LinkedHashMap<AIDM_Pattern, Double>();
		for (Map.Entry<AIDM_Pattern, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}
	
	public static HashMap<AIDM_Pattern, Double> QuicksortByValue(HashMap<AIDM_Pattern, Double> hm) {
        AIDM_Sorting li = new AIDM_Sorting();

		for(AIDM_Pattern p : hm.keySet()) {
			li.push(p, hm.get(p));
		}
        li.head = li.mergeSort(li.head);
        
		return li.getSortedMap(li.head);
	}

	public static HashMap<String, Double> sortByValueName(HashMap<String, Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static HashMap<AIDM_Pattern, Integer> sortByValue_int(HashMap<AIDM_Pattern, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<AIDM_Pattern, Integer>> list = new LinkedList<Map.Entry<AIDM_Pattern, Integer>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<AIDM_Pattern, Integer>>() {
			public int compare(Map.Entry<AIDM_Pattern, Integer> o1, Map.Entry<AIDM_Pattern, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<AIDM_Pattern, Integer> temp = new LinkedHashMap<AIDM_Pattern, Integer>();
		for (Map.Entry<AIDM_Pattern, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static HashMap<AIDM_Measure, Integer> sortByValueint(HashMap<AIDM_Measure, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<AIDM_Measure, Integer>> list = new LinkedList<Map.Entry<AIDM_Measure, Integer>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<AIDM_Measure, Integer>>() {
			public int compare(Map.Entry<AIDM_Measure, Integer> o1, Map.Entry<AIDM_Measure, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<AIDM_Measure, Integer> temp = new LinkedHashMap<AIDM_Measure, Integer>();
		for (Map.Entry<AIDM_Measure, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static HashMap<AIDM_Pattern, Double> sortByValueDes(HashMap<AIDM_Pattern, Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<AIDM_Pattern, Double>> list = new LinkedList<Map.Entry<AIDM_Pattern, Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<AIDM_Pattern, Double>>() {
			public int compare(Map.Entry<AIDM_Pattern, Double> o1, Map.Entry<AIDM_Pattern, Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<AIDM_Pattern, Double> temp = new LinkedHashMap<AIDM_Pattern, Double>();
		for (Map.Entry<AIDM_Pattern, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static HashMap<AIDM_Pattern[], Double> sortByValue1(HashMap<AIDM_Pattern[], Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<AIDM_Pattern[], Double>> list = new LinkedList<Map.Entry<AIDM_Pattern[], Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<AIDM_Pattern[], Double>>() {
			public int compare(Map.Entry<AIDM_Pattern[], Double> o1, Map.Entry<AIDM_Pattern[], Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<AIDM_Pattern[], Double> temp = new LinkedHashMap<AIDM_Pattern[], Double>();
		for (Map.Entry<AIDM_Pattern[], Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static HashMap<AIDM_Pattern[], Double> sortByValueASC(HashMap<AIDM_Pattern[], Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<AIDM_Pattern[], Double>> list = new LinkedList<Map.Entry<AIDM_Pattern[], Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<AIDM_Pattern[], Double>>() {
			public int compare(Map.Entry<AIDM_Pattern[], Double> o1, Map.Entry<AIDM_Pattern[], Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<AIDM_Pattern[], Double> temp = new LinkedHashMap<AIDM_Pattern[], Double>();
		for (Map.Entry<AIDM_Pattern[], Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public HashMap<String, Double> sortByValueM(HashMap<String, Double> hm) {

		// Create a list from elements of HashMap
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public void printprocessstat(Process p) {
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// Read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		String s = null;
		try {
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// Read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void GenerateTex() {

	}

}
