package cz.vutbr.fit.mis.dip.perfserver.controller.objects;

import java.text.DecimalFormat;

import cz.vutbr.fit.mis.dip.perfserver.enums.Const;

public class SummaryData {
	private Double mean;
	private Double min;
	private Double max;
	private Double stddev;
	private DecimalFormat df = new DecimalFormat(Const.PERCENTAGE);

	public SummaryData() {
	}

	public SummaryData(Double mean, Double min, Double max, Double stddev) {
		this.mean = mean;
		this.min = min;
		this.max = max;
		this.stddev = stddev;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getStddev() {
		return stddev;
	}

	public void setStddev(Double stddev) {
		this.stddev = stddev;
	}
	
	public Double getRange() {
		return max - min;
	}
	
	public String getCoefVarInPercentage() {
		return (stddev == null || mean == 0.0)? null : df.format(stddev / mean * 100) + " %";
	}
}
