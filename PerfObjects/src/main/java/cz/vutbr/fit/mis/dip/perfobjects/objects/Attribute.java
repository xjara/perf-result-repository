package cz.vutbr.fit.mis.dip.perfobjects.objects;

import java.util.LinkedList;
import java.util.List;


public class Attribute {
	private String unit;
	private List<Result> results;
	
	public Attribute() {		
	}
	
	public Attribute(String unit) {
		this.unit = unit;
		results = new LinkedList<Result>();
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}
}
