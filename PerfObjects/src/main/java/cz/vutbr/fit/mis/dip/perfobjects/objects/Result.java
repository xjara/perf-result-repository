package cz.vutbr.fit.mis.dip.perfobjects.objects;

public class Result {
	private String date;
	private double value;

	public Result() {
	}
	
	public Result(String date, double value) {
		this.date = date;
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
