package cz.vutbr.fit.mis.dip.perfserver.controller.objects;

import java.util.Date;

public class TestSuiteValue {
	private int id;
	private Date startTime;
	private boolean finished;
	private String hw;
	private Double value;
	
	public TestSuiteValue(int id, Date startTime, boolean finished, String hw, Double value) {
		this.id = id;
		this.startTime = startTime;
		this.finished = finished;
		this.hw = hw;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean getFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getHw() {
		return hw;
	}

	public void setHw(String hw) {
		this.hw = hw;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
