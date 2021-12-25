package RankSVM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import AIDM_Pattern.AIDM_Pattern;
import AIDM_Query.AIDM_Query;
import AIDM_Utils.AIDM_Utils;

public class RankSVM {

	double C;
	static long pid ;

	static Process p;
	static String h ;
	static double iterations ;
	static long timeout ;

	public RankSVM(double C, String h,double iterations,long timeout) {
		this.C = C;
		this.h=h;
		this.iterations=iterations;
		this.timeout=timeout;
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();

        // Get name representing the running Java virtual machine.
        // It returns something like 6460@AURORA. Where the value
        // before the @ symbol is the PID.
        String jvmName = bean.getName();

        // Extract the PID by splitting the string returned by the
        // bean.getName() method.
        pid = Long.valueOf(jvmName.split("@")[0]);
        
        Init();
	}
	
	
	
		


		
	


	private void Init() {
		Arrays.stream(new File("SVM_"+h+"/model/").listFiles()).forEach(File::delete);
		Arrays.stream(new File("SVM_"+h+"/data/").listFiles()).forEach(File::delete);


	}










	public void learn(double i) throws Exception {
		try {
			double c=C*i;
			String[] cmd = { "/bin/sh", "Learn.sh", c+"" ,"data/train_"+pid+".dat", " model/model"+pid+".dat" };
			Runtime rt = Runtime.getRuntime();
			String line="";
		
			p = rt.exec(cmd, cmd, new File("SVM_"+h+"/"));
			/*InputStreamReader isr = new InputStreamReader(p.getInputStream());
			BufferedReader rdr = new BufferedReader(isr);
			while((line = rdr.readLine()) != null) { 
			  System.out.println(line);
			} 
			isr = new InputStreamReader(p.getErrorStream());
			rdr = new BufferedReader(isr);
			while((line = rdr.readLine()) != null) { 
			  System.out.println(line);
			}*/ 
			p.waitFor(timeout, TimeUnit.MILLISECONDS);
			p.destroy();
			p.waitFor(); 
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public static double[] getWeights(int n) {
		double[] weights = new double[n];
		FileReader file;
		String CurrentLine = "";
		String lastline = "";
		try {
		File f = new File("SVM_"+h+"/model/model"+pid+".dat");

		

			file = new FileReader(f );
			
			BufferedReader br = new BufferedReader(file);
			while ((CurrentLine = br.readLine()) != null) {
				lastline = CurrentLine;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e);	
			System.err.println("reached timeout");	
			}
		int i = 0;
		for (String s : lastline.split(":")) {
			if (i == 0) {
				i++;
				continue;
			}
			weights[i-1]=Double.parseDouble(s.split(" ")[0]);
			i++;
		}
		
		
	

		return weights;
	}

	public static void BuildDataset(AIDM_Query query, int i ) {
		try {
			File f1 = new File("SVM_"+h+"/data/train_"+pid+".dat");
			if (!f1.exists()) {
				f1.createNewFile();
			}

			FileWriter fileWritter = new FileWriter(f1, true);
			BufferedWriter bw = new BufferedWriter(fileWritter);
			PrintWriter pr = new PrintWriter(bw);
			//i=getLastIndex(f1)+1;
			int value=1;
			query.sortByRank();
			for (AIDM_Pattern p : query.Patterns) {
							//System.out.println(p.getShortname()+" :: "+ p.getUserRank() );
							String line1 = value + " qid:" + (i) + " ";
							for (int j = 0; j < p.getVectorMeasures().length; j++)
								line1 += (j + 1) + ":" + p.getVectorMeasures()[j] + " ";

							pr.println(line1);
							value++;

						}

			
			pr.close();
			bw.close();
			fileWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int getLastIndex(File file) {
		int i = 1 ;

		try {FileReader fr = new FileReader(file);

		String CurrentLine = "";
		String lastline = "";
		
			BufferedReader br = new BufferedReader(fr);
			while ((CurrentLine = br.readLine()) != null) {
				lastline = CurrentLine;
			}
			if(!lastline.equals(""))
				i=Integer.parseInt((lastline.split(" ")[1]).split(":")[1]);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}


	public void PredictSVMRanking(AIDM_Query query,double[] weights) {
		HashMap<AIDM_Pattern,Double> patterns = new HashMap<AIDM_Pattern,Double> ();
		for (AIDM_Pattern p : query.Patterns) {
			double score =p.PredictScore(weights);
			patterns.put(p,score);
		}
        
		HashMap<AIDM_Pattern,Double> sorted = AIDM_Utils.sortByValueDes(patterns);
				
		
		int i=1;
		for (AIDM_Pattern p : sorted.keySet()) {

			p.setRank("svm",i);
			i++;

		}
	}
	
}
