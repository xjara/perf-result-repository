package cz.vutbr.fit.mis.dip.perfclient.attrs;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.vutbr.fit.mis.dip.perfobjects.constants.Format;


public class MAtt {
	protected String name;
	protected String unit;
	protected double value;
	protected String time;
	protected SimpleDateFormat sdf = new SimpleDateFormat(Format.DATE_TIME);
	
	public MAtt(String name, String unit) {
		this.name = name;
		this.unit = unit;
		this.time = null;	// time is not measured by default 
	}
	
	public MAtt(String name, String unit, double value) {
		this.name = name;
		this.unit = unit;
		this.value = value;
		gainAndSetTime();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public String getTime() {
		return time;
	}
	
	public void gainAndSetTime() {
		time = sdf.format(new Date());
	}
}
