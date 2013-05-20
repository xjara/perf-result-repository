package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import cz.vutbr.fit.mis.dip.perfserver.dao.ResultDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Result;


@Stateless
public class ResultDaoImpl implements ResultDao {
	@Inject
	private EntityManager em;

	private Double buildAndExecuteQuery(TypedQuery<Double> query, long testSuiteRunId, long methodId, Long attributeId) {
		return query.setParameter("testSuiteRunId", testSuiteRunId)
						.setParameter("methodId", methodId)
						.setParameter("attributeId", attributeId)
						.getSingleResult();
	}
	
	@Override
	public List<Result> getAttributeResultsOrderById(long attrResultId) {
		return em.createNamedQuery("attributeResultsOrderById", Result.class)
					.setParameter("attrResultId", attrResultId)
					.getResultList();
	}
	
	@Override
	public Double getAvgResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(long testSuiteRunId,
			long methodId, long attributeId) {
	
		TypedQuery<Double> query = em.createNamedQuery("getAvgResultByTestSuiteRunIdAndByMethodIdAndByAttributeId", Double.class);
		return buildAndExecuteQuery(query, testSuiteRunId, methodId, attributeId);
	}
	
	@Override
	public Double getMinResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(long testSuiteRunId,
			long methodId, long attributeId) {
		
		TypedQuery<Double> query = em.createNamedQuery("getMinResultByTestSuiteRunIdAndByMethodIdAndByAttributeId", Double.class);
		return buildAndExecuteQuery(query, testSuiteRunId, methodId, attributeId);
	}

	@Override
	public Double getMaxResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(long testSuiteRunId,
			long methodId, long attributeId) {
		
		TypedQuery<Double> query = em.createNamedQuery("getMaxResultByTestSuiteRunIdAndByMethodIdAndByAttributeId", Double.class);
		return buildAndExecuteQuery(query, testSuiteRunId, methodId, attributeId);
	}
	
	@Override
	public Double getAvgAttributeResultsValue(long attrResultId) {
		return em.createNamedQuery("getAvgAttributeResultsValue", Double.class)
					.setParameter("attrResultId", attrResultId)
					.getSingleResult();
	}

	@Override
	public Double getMinAttributeResultsValue(long attrResultId) {
		return em.createNamedQuery("getMinAttributeResultsValue", Double.class)
					.setParameter("attrResultId", attrResultId)
					.getSingleResult();
	}

	@Override
	public Double getMaxAttributeResultsValue(long attrResultId) {
		return em.createNamedQuery("getMaxAttributeResultsValue", Double.class)
					.setParameter("attrResultId", attrResultId)
					.getSingleResult();
	}

	@Override
	public Double getStdDevAttributeResultsValue(long attrResultId) {
		return (Double) em.createNativeQuery("select stddev(value) from result where attrresult_id = :attrResultId")
							.setParameter("attrResultId", attrResultId)
							.getSingleResult();
	}
}
