package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.ThresholdDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Threshold;



@Stateless
public class ThresholdDaoImpl implements ThresholdDao {
	@Inject
	private EntityManager em;
	
	@Override
	public List<Threshold> getGlobalThresholds() {
		return em.createNamedQuery("globalThresholds", Threshold.class).getResultList();
	}

	@Override
	public Threshold getGlobalThresholdByAttrName(String attr) {
		List<Threshold> thresholds = em.createNamedQuery("globalThresholdsByAttr", Threshold.class)
										.setParameter("attr", attr)
										.setMaxResults(2)
										.getResultList();
		
		return thresholds.isEmpty()? null : thresholds.get(0); 
	}

	@Override
	public Threshold getGlobalThresholdByUnitId(long unitId) {
		List<Threshold> thresholds = em.createNamedQuery("globalThresholdsByUnitId", Threshold.class)
										.setParameter("unitId", unitId)
										.setMaxResults(2)
										.getResultList();
		
		return thresholds.isEmpty()? null : thresholds.get(0);
	}
	
	@Override
	public Threshold getGlobalThresholdByAttrId(long attrId) {
		List<Threshold> thresholds = em.createNamedQuery("globalThresholdsByAttrId", Threshold.class)
				.setParameter("attrId", attrId)
				.setMaxResults(2)
				.getResultList();

		return thresholds.isEmpty()? null : thresholds.get(0);
	}
	
	@Override
	public Threshold getLocalThresholdByUnitId(long unitId) {
		List<Threshold> thresholds = em.createNamedQuery("localThresholdsByUnitId", Threshold.class)
										.setParameter("unitId", unitId)
										.setMaxResults(2)
										.getResultList();
		
		return thresholds.isEmpty()? null : thresholds.get(0);
	}
	
	@Override
	public Threshold getLocalThresholdByAttrId(long attrId) {
		List<Threshold> thresholds = em.createNamedQuery("localThresholdsByAttrId", Threshold.class)
				.setParameter("attrId", attrId)
				.setMaxResults(2)
				.getResultList();

return thresholds.isEmpty()? null : thresholds.get(0);
	}
	
	@Override
	public Threshold getProjectLocalThresholdByAttrName(String project, String attr) {
		List<Threshold> thresholds = em.createNamedQuery("localThresholdsByProjectAndByAttr", Threshold.class)
										.setParameter("project", project)
										.setParameter("attr", attr)
										.setMaxResults(2)
										.getResultList();
		
		return thresholds.isEmpty()? null : thresholds.get(0);
	}

	@Override
	public List<Threshold> getProjectLocalThresholds(String project) {
		return em.createNamedQuery("localThresholdsByProject", Threshold.class)
					.setParameter("project", project)
					.getResultList();
	}

	@Override
	public Threshold getLocalThresholdByProjectAndAttr(long projectId, String attr) {
		List<Threshold> thresholds = em.createNamedQuery("localThresholdsByProjectIdAndByAttr", Threshold.class)
										.setParameter("projectId", projectId)
										.setParameter("attr", attr)
										.setMaxResults(2)
										.getResultList();
		
		return thresholds.isEmpty()? null : thresholds.get(0);
	}
	
	@Override
	public void save(Threshold threshold) {
		em.merge(threshold);
	}

	@Override
	public void remove(Threshold threshold) {
		em.remove(em.merge(threshold));
	}
}
