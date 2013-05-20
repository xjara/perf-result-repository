package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.UnitDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Unit;



@Stateless
public class UnitDaoImpl implements UnitDao {
	@Inject
	private EntityManager em;
	
	@Override
	public List<Unit> getUnits() {
		return em.createNamedQuery("units", Unit.class).getResultList();
	}
	
	@Override
	public Unit getUnitByName(String unit) {
		List<Unit> units = em.createNamedQuery("unitsByName", Unit.class)
								.setParameter("unit", unit)
								.setMaxResults(2)
								.getResultList();
		
		return units.isEmpty()? null : units.get(0);
	}
	
	@Override
	public void save(Unit unit) {
		em.merge(unit);
	}

	@Override
	public void remove(Unit unit) {
		em.remove(em.merge(unit));
	}
}
