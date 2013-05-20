package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Result;


public interface ResultDao {
	public List<Result> getAttributeResultsOrderById(long attrResultId);
	public Double getAvgResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(long testSuiteRunId, long methodId, long attributeId);
	public Double getMinResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(long testSuiteRunId, long methodId, long attributeId);
	public Double getMaxResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(long testSuiteRunId, long methodId, long attributeId);
	public Double getAvgAttributeResultsValue(long attrResultId);
	public Double getMinAttributeResultsValue(long attrResultId);
	public Double getMaxAttributeResultsValue(long attrResultId);
	public Double getStdDevAttributeResultsValue(long attrResultId);
}
