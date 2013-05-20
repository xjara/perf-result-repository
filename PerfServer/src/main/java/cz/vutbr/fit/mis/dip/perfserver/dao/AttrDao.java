package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Attr;


public interface AttrDao {
	public List<Attr> getAttrs();
	public Attr getAttrByName(String attr);
	public Attr getAttrById(long attrId);
	public Attr getAttrByUnit(String unit);
	public void save(Attr attr);
	public void remove(Attr attr);
}
