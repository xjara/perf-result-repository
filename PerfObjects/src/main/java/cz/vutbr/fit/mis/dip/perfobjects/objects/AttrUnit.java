package cz.vutbr.fit.mis.dip.perfobjects.objects;

import cz.vutbr.fit.mis.dip.perfobjects.constants.AttrUnitStatus;

public class AttrUnit {
	private String attr;
	private String unit;
	private AttrUnitStatus status;
	
	public AttrUnit() {
	}
	
	public AttrUnit(String attr, String unit) {
		this.attr = attr;
		this.unit = unit;
	}	

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public AttrUnitStatus getStatus() {
		return status;
	}

	public void setStatus(AttrUnitStatus status) {
		this.status = status;
	}
}
