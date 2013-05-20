package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.AttrResultDao;
import cz.vutbr.fit.mis.dip.perfserver.model.AttrResult;



@Stateless
public class AttrResultDaoImpl implements AttrResultDao {
	@Inject
	private EntityManager em;
	
	@Override
	public AttrResult save(AttrResult attrResult) {
		return em.merge(attrResult);
	}
	
	@Override
	public List<AttrResult> getAttrResultByTestRun(long testRunId) {
		return em.createNamedQuery("attrResultByTestRunId", AttrResult.class)
					.setParameter("testRunId", testRunId)
					.getResultList();
	}

	@Override
	public AttrResult getFirstUsedAttrResultByAttrId(long attrId) {
		List<AttrResult> attrResults =  em.createNamedQuery("attrResultByAttrId", AttrResult.class)
											.setParameter("attrId", attrId)
											.setMaxResults(2)
											.getResultList();
		
		return attrResults.isEmpty()? null : attrResults.get(0);
	}
	
	@Override
	public AttrResult getFirstUsedAttrResultByUnitId(long unitId) {
		List<AttrResult> attrResults = em.createNamedQuery("attrResultByUnitId", AttrResult.class)
											.setParameter("unitId", unitId)
											.setMaxResults(2)
											.getResultList();
		
		return attrResults.isEmpty()? null : attrResults.get(0); 
	}

	@Override
	public List<AttrResult> getAttrResultByMethod(long methodId) {
		return em.createNamedQuery("attrResultByMethodId", AttrResult.class)
				.setParameter("methodId", methodId)
				.getResultList();
	}

	@Override
	public AttrResult getAttrResultByTestRunIdAndByAttrId(long testRunId,
			long attrId) {
		List<AttrResult> attrResults = em.createNamedQuery("attrResultByTestRunIdAndByAttrId", AttrResult.class)
											.setParameter("testRunId", testRunId)
											.setParameter("attrId", attrId)
											.setMaxResults(2)
											.getResultList();
		
		return attrResults.isEmpty()? null : attrResults.get(0);
	}
}
