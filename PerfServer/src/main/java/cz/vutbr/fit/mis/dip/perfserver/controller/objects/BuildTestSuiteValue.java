package cz.vutbr.fit.mis.dip.perfserver.controller.objects;

import java.util.Date;

public class BuildTestSuiteValue extends TestSuiteValue {
	
	private String build;
	
	public BuildTestSuiteValue(int id, String build, Date startTime, boolean finished,
			String hw, Double value) {
		super(id, startTime, finished, hw, value);
		this.setBuild(build);
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}
}
