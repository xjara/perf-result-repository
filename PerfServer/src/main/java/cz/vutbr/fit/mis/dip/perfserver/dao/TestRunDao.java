package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;


public interface TestRunDao {
	public TestRun save(TestRun testRun);
	public TestRun getTestRunById(Long testRunId);
	public List<TestRun> getAllTestRuns(long testSuiteRunId);
	public List<TestRun> getSuccessfullTestRuns(long testSuiteRunId);
	public List<TestRun> getFailedTestRuns(long testSuiteRunId);
	public TestRun getTestRunByTestSuiteRunIdAndByMethod(long testSuiteRunId, String method);
	public TestRun getTestRunByTestSuiteRunIdAndByMethodId(long testSuiteRunId, long methodId);
	public TestRun getTestedTestRunByTestSuiteRunId(long testSuiteRunId);
	public List<TestRun> getTestRunsForView3(long projectId, List<String> builds, String testSuite, long methodId);
}
