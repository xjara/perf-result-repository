package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.AttrDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;


@Stateless
public class AttrDaoImpl implements AttrDao {
	@Inject
	private EntityManager em;

	@Override
	public List<Attr> getAttrs() {
		return em.createNamedQuery("attrs", Attr.class).getResultList();
	}
	
	@Override
	public Attr getAttrById(long attrId) {
		List<Attr> attrs = em.createNamedQuery("attrsById", Attr.class)
				.setParameter("attrId", attrId)
				.setMaxResults(2)
				.getResultList();

		return attrs.isEmpty()? null : attrs.get(0);
	}	
	
	@Override
	public Attr getAttrByName(String attr) {
		List<Attr> attrs = em.createNamedQuery("attrsByName", Attr.class)
								.setParameter("attr", attr)
								.setMaxResults(2)
								.getResultList();
		
		return attrs.isEmpty()? null : attrs.get(0);
	}
	
	@Override
	public Attr getAttrByUnit(String unit) {
		List<Attr> attrs = em.createNamedQuery("attrsByUnit", Attr.class)
								.setParameter("unit", unit)
								.setMaxResults(2)
								.getResultList();
		
		return attrs.isEmpty()? null : attrs.get(0);
	}

	@Override
	public void save(Attr attr) {
		em.merge(attr);
	}
	
	@Override
	public void remove(Attr attr) {
		em.remove(em.merge(attr));
	}
}
