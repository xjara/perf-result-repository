package cz.vutbr.fit.mis.dip.perfobjects.constants;

public enum AttrUnitStatus {
	ATTR_UNIT_OK(0),				// attribute and its unit are defined
	ATTR_ERR(1),					// attribute is not defined, but its unit is 
	UNIT_ERR(2),					// attribute is defined, but its unit not
	UNIT_ERR_ASSIGNED(3),			// attribute is defined, but has assigned undefined unit
	ATTR_UNIT_ERR(4);				// attribute and its unit are not defined
	
	private int code;
	
	private AttrUnitStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
