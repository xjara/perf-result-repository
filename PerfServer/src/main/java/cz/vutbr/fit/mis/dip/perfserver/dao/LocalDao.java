package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Local;

public interface LocalDao {
	List<Local> getLocalsByProjectId(long projectId);
}
