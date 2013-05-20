package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.TestRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;



@Stateless
public class TestRunDaoImpl implements TestRunDao {
	@Inject
	private EntityManager em;
	
	@Override
	public TestRun save(TestRun testRun) {
		return em.merge(testRun);
	}
	
	@Override
	public TestRun getTestRunById(Long testRunId) {
		return em.find(TestRun.class, testRunId);
	}
	
	@Override
	public List<TestRun> getAllTestRuns(long testSuiteRunId) {
		return em.createNamedQuery("allTestRunsByTestSuiteRunId", TestRun.class)
					.setParameter("testSuiteRunId", testSuiteRunId)
					.getResultList();
	}
	
	@Override
	public List<TestRun> getSuccessfullTestRuns(long testSuiteRunId) {
		return em.createNamedQuery("successfullTestRunsByTestSuiteRunId", TestRun.class)
					.setParameter("testSuiteRunId", testSuiteRunId)
					.getResultList();
	}

	@Override
	public List<TestRun> getFailedTestRuns(long testSuiteRunId) {
		return em.createNamedQuery("failedTestRunsByTestSuiteRunId", TestRun.class)
					.setParameter("testSuiteRunId", testSuiteRunId)
					.getResultList();
	}

	@Override
	public TestRun getTestRunByTestSuiteRunIdAndByMethod(long testSuiteRunId,
			String method) {
		List<TestRun> testRuns = em.createNamedQuery("testRunsByTestSuiteRunIdAndByMethod", TestRun.class)
									.setParameter("testSuiteRunId", testSuiteRunId)
									.setParameter("method", method)
									.setMaxResults(2)
									.getResultList();
		
		return testRuns.isEmpty()? null : testRuns.get(0);
	}

	@Override
	public TestRun getTestRunByTestSuiteRunIdAndByMethodId(long testSuiteRunId,
			long methodId) {
		List<TestRun> testRuns = em.createNamedQuery("testRunsByTestSuiteRunIdAndByMethodId", TestRun.class)
				.setParameter("testSuiteRunId", testSuiteRunId)
				.setParameter("methodId", methodId)
				.setMaxResults(2)
				.getResultList();

		return testRuns.isEmpty()? null : testRuns.get(0);
	}

	@Override
	public TestRun getTestedTestRunByTestSuiteRunId(long testSuiteRunId) {
		List<TestRun> testRuns = em.createNamedQuery("testedTestRunByTestSuiteRunId", TestRun.class)
									.setParameter("testSuiteRunId", testSuiteRunId)
									.setMaxResults(2)
									.getResultList();
		
		return testRuns.isEmpty()? null : testRuns.get(0);
	}

	@Override
	public List<TestRun> getTestRunsForView3(long projectId,
			List<String> builds, String testSuite, long methodId) {
		
		return em.createNamedQuery("testRunsByProjectIdAndByBuildsAndByTestSuiteAndByMethodIdByStartTimeDesc", TestRun.class)
					.setParameter("projectId", projectId)
					.setParameter("builds", builds)
					.setParameter("testSuite", testSuite)
					.setParameter("methodId", methodId)
					.getResultList();							
	}
}
 