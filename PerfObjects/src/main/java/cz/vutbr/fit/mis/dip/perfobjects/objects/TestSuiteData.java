package cz.vutbr.fit.mis.dip.perfobjects.objects;

public class TestSuiteData {
	private String project; 
	private String build;
	private String hw;
	private String startTime;
	
	public TestSuiteData() {
	}

	public TestSuiteData(String project, String build, String hw, String startTime) {
		this.project = project;
		this.build = build;
		this.hw = hw;
		this.startTime = startTime;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getHw() {
		return hw;
	}

	public void setHw(String hw) {
		this.hw = hw;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
}
