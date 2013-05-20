package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Hw;


public interface HwDao {
	public List<Hw> getHws();
	public Hw getHwByName(String hw);
	public List<Hw> getUniqueHwsByProjectIdAndByBuildAndByTestSuite(long projectId, String build, String testSuite);
	public Hw save(Hw hw);
}
