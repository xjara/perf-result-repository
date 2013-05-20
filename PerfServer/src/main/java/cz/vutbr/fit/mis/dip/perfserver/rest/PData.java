package cz.vutbr.fit.mis.dip.perfserver.rest;

public class PData {
	private String u;
	private Double bt;
	private Double t;
	private Double d;
	private Double p;
	private String th;
	
	public PData() {
	}
	
	public PData(String u, Double bt, Double t, Double d, Double p, String th) {
		this.u = u;
		this.bt = bt;
		this.t = t;
		this.d = d;
		this.p = p;
		this.th = th;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public Double getBT() {
		return bt;
	}

	public void setBT(Double bt) {
		this.bt = bt;
	}

	public Double getT() {
		return t;
	}

	public void setT(Double t) {
		this.t = t;
	}

	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}

	public Double getP() {
		return p;
	}

	public void setP(Double p) {
		this.p = p;
	}

	public String getTh() {
		return th;
	}

	public void setTh(String th) {
		this.th = th;
	}
}
