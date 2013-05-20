package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.TestSuite;


public interface TestSuiteDao {
	public TestSuite save(TestSuite testSuite);
	public TestSuite getTestSuiteByNameAndByProjectAndByBuild(String testSuite, String project, String build);
	public TestSuite getTestSuiteByNameAndByBuildId(String testSuite, Long buildId);
	public List<String> getUniqueTestSuiteNamesByProjectId(long projectId);
}
