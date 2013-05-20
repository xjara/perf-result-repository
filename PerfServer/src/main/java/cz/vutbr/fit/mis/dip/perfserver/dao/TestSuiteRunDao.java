package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.Date;
import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;

public interface TestSuiteRunDao {
	public TestSuiteRun save(TestSuiteRun testSuiteRun);
	public void remove(TestSuiteRun testSuiteRun);
	public TestSuiteRun getTestSuiteRunById(Long testSuiteRunId);
	public List<TestSuiteRun> getTestSuiteRunsByProjectIdByStartTimeDesc(Long projectId);
	public TestSuiteRun getLastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild(String testSuite, String project, String build);
	public TestSuiteRun getBeforeLastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild(String testSuite, String project, String build);
	public List<TestSuiteRun> getActuallyTestedTestSuiteRunsByStartTimeDesc();
	public List<TestSuiteRun> getFinishedTestSuiteRunsInTimeRangeByStartTimeDesc(Date start, Date end);
	public List<TestSuiteRun> getFinishedTestSuiteRunsFromDateByStartTimeDesc(Date start);
	public List<TestSuiteRun> getTestSuiteRunsForView1(long projectId, String build, String testSuite, List<Long> chosenHws);
	public List<TestSuiteRun> getTestSuiteRunsByProjectIdAndByBuildAndByTestSuite(long projectId, String build, String testSuite);
	public TestSuiteRun getFirstTestSuiteRunByProjectIdAndByTestSuite(long projectId, String testSuite);
}
