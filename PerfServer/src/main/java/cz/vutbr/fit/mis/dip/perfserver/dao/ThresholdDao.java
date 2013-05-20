package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Threshold;


public interface ThresholdDao {
	public List<Threshold> getGlobalThresholds();
	public Threshold getGlobalThresholdByAttrName(String attrName);
	public Threshold getGlobalThresholdByUnitId(long unitId);
	public Threshold getGlobalThresholdByAttrId(long attrId);
	public Threshold getLocalThresholdByUnitId(long unitId);
	public Threshold getLocalThresholdByAttrId(long attrId);
	public Threshold getLocalThresholdByProjectAndAttr(long projectId, String attrName);
	public Threshold getProjectLocalThresholdByAttrName(String projectName, String attrName);
	public List<Threshold> getProjectLocalThresholds(String projectName);
	public void save(Threshold threshold);
	public void remove(Threshold threshold);
}
