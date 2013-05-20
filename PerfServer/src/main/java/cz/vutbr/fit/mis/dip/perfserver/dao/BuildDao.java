package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Build;


public interface BuildDao {
	public List<Build> getBuildsByProjectId(Long projectId);
	public Build getBuildByNameAndByProjectId(String build, Long projectId);
	public Build getBuildByNameAndByProject(String build, String project);
	public Build save(Build build);
	public List<String> getUniqueBuildNamesByProjectIdAndByTestSuite(Long projectId, String testSuite);
}
