package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Unit;


public interface UnitDao {
	public List<Unit> getUnits();
	public Unit getUnitByName(String unitName);
	public void save(Unit unit);
	public void remove(Unit unit);
}
