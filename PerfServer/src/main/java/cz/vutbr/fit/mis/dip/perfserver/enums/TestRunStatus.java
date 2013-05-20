package cz.vutbr.fit.mis.dip.perfserver.enums;

public enum TestRunStatus {
	FAILED(0),
	SUCCESSFULL(1),
	EXCLUDED(2),
	TESTED(3);
	
	private int code;
	
	private TestRunStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
