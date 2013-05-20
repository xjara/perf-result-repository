package cz.vutbr.fit.mis.dip.perfobjects.objects;

import java.util.List;


public class LastTestSuiteData {
	private String endTime;
	private List<String> excludedMethods;
	
	public LastTestSuiteData() {
	}
	
	public LastTestSuiteData(String endTime, List<String> excludedMethods) {
		this.endTime = endTime;
		this.excludedMethods = excludedMethods;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<String> getExcludedMethods() {
		return excludedMethods;
	}

	public void setExcludedMethods(List<String> excludedMethods) {
		this.excludedMethods = excludedMethods;
	}
}
