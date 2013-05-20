package cz.vutbr.fit.mis.dip.perfserver.enums;

public enum ThresholdStatus {
	NONE(null),
	GLOBAL("G"),
	LOCAL("L");
	
	private String flag;
	
	private ThresholdStatus(String flag) {
		this.flag = flag;
	}
	
	public String getFlag() {
		return flag;
	}
}
