package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.HwDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Hw;


@Stateless
public class HwDaoImpl implements HwDao {
	@Inject
	private EntityManager em;
	
	@Override
	public List<Hw> getHws() {
		return em.createNamedQuery("hws", Hw.class)
					.getResultList();
	}
	
	@Override
	public Hw getHwByName(String hw) {
		List<Hw> hws = em.createNamedQuery("hwsByName", Hw.class)
							.setParameter("hw", hw)
							.setMaxResults(2)
							.getResultList();
		
		return hws.isEmpty()? null : hws.get(0);
	}

	@Override
	public Hw save(Hw hw) {
		return em.merge(hw);
	}

	@Override
	public List<Hw> getUniqueHwsByProjectIdAndByBuildAndByTestSuite(
			long projectId, String build, String testSuite) {
		
		List<Hw> hws =  em.createNamedQuery("hwsByProjectIdAndByBuildAndByTestSuite", Hw.class)
							.setParameter("projectId", projectId)
							.setParameter("build", build)
							.setParameter("testSuite", testSuite)
							.getResultList();
		
		return new LinkedList<Hw>(new HashSet<Hw>(hws));
	}
}
