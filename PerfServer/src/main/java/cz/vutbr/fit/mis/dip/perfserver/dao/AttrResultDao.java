package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.AttrResult;


public interface AttrResultDao {
	public AttrResult save(AttrResult attrResult);
	public List<AttrResult> getAttrResultByTestRun(long testRunId);
	public AttrResult getFirstUsedAttrResultByAttrId(long attrId);
	public AttrResult getFirstUsedAttrResultByUnitId(long unitId);
	public List<AttrResult> getAttrResultByMethod(long methodId);
	public AttrResult getAttrResultByTestRunIdAndByAttrId(long testRunId, long attrId);
}
