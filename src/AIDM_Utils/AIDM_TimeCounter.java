package AIDM_Utils;

import java.util.ArrayList;
import java.util.Collections;

public class AIDM_TimeCounter {

	public ArrayList<Long> times;
	public ArrayList<Long> genTime;

	public int queries;

	public AIDM_TimeCounter() {
		this.times = new ArrayList<Long>();
		this.genTime = new ArrayList<Long>();

	}

	public void Waiting_Time(long elapsed) {
		this.times.add(elapsed);

	}

	public void setQueries(int queries) {
		this.queries = queries;
	}

	public double getQueries() {

		return this.queries;
	}

	public double GetAVGWaitingTime() {
		double time = 0.0;
		for (Long t : times) {
			time += t;
		}
		return time;
	}

	public double GetWaitingTime() {
		if(times.size()>0)
		return times.get(times.size() - 1);
		else 
			return 0.0;
	}

	public long getGenerationTime() {
		if (genTime.isEmpty())
			return 0;
		return Collections.max(genTime);
	}

}
