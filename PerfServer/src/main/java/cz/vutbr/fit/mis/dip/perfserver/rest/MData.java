package cz.vutbr.fit.mis.dip.perfserver.rest;

import java.util.Map;

public class MData {
	private String method;
	private Map<String, PData> mData;
	
	public MData() {
	}

	public MData(String method, Map<String, PData> mData) {
		this.method = method;
		this.mData = mData;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, PData> getmData() {
		return mData;
	}

	public void setmData(Map<String, PData> mData) {
		this.mData = mData;
	}
}
