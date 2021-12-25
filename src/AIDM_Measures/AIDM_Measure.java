package AIDM_Measures;


public class AIDM_Measure {

	public  String name;
	public  double value;


	public AIDM_Measure(String name , double value) {
		this.name=name;
		this.value=value;
	}

	

	public AIDM_Measure() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "AIDM_Measure [name=" + name + ", value=" + value + "]";
	}


}
