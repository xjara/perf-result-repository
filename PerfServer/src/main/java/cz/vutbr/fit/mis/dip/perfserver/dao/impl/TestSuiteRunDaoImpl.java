package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;

@Stateless
public class TestSuiteRunDaoImpl implements TestSuiteRunDao {
	@Inject
	private EntityManager em;
	
	private List<TestSuiteRun> getLastFinishedTestSuiteRunsByTestSuiteAndByProjectAndByBuild(String testSuite, String project, String build) {
		return em.createNamedQuery("lastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild", TestSuiteRun.class)
					.setParameter("testSuite", testSuite)
					.setParameter("project", project)
					.setParameter("build", build)
					.setMaxResults(2)
					.getResultList();
	}
	
	@Override
	public TestSuiteRun save(TestSuiteRun testSuiteRun) {
		return em.merge(testSuiteRun);
	}

	@Override
	public void remove(TestSuiteRun testSuiteRun) {
		testSuiteRun = em.find(TestSuiteRun.class, testSuiteRun.getId());
		em.remove(testSuiteRun);
	}
	
	@Override
	public TestSuiteRun getTestSuiteRunById(Long testSuiteRunId) {
		return em.find(TestSuiteRun.class, testSuiteRunId);
	}
	
	@Override
	public List<TestSuiteRun> getTestSuiteRunsByProjectIdByStartTimeDesc(Long projectId) {
		return em.createNamedQuery("testSuiteRunsByProjectIdByStartTimeDesc", TestSuiteRun.class)
					.setParameter("projectId", projectId)
					.getResultList();
	}

	@Override
	public TestSuiteRun getLastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild(String testSuite, String project, String build) {
		List<TestSuiteRun> testSuiteRuns = getLastFinishedTestSuiteRunsByTestSuiteAndByProjectAndByBuild(testSuite, project, build);
		return testSuiteRuns.isEmpty()? null : testSuiteRuns.get(0);
	}

	@Override
	public TestSuiteRun getBeforeLastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild(String testSuite, String project, String build) {
		List<TestSuiteRun> testSuiteRuns = getLastFinishedTestSuiteRunsByTestSuiteAndByProjectAndByBuild(testSuite, project, build);
		return testSuiteRuns.size() == 2? testSuiteRuns.get(1) : null;
	}

	@Override
	public List<TestSuiteRun> getActuallyTestedTestSuiteRunsByStartTimeDesc() {
		return em.createNamedQuery("actuallyTestedTestSuiteRunsByStartTimeDesc", TestSuiteRun.class).getResultList();
	}

	@Override
	public List<TestSuiteRun> getFinishedTestSuiteRunsInTimeRangeByStartTimeDesc(Date start, Date end) {
		return em.createNamedQuery("finishedTestSuiteRunsInTimeRangeByStartTimeDesc", TestSuiteRun.class)
					.setParameter("start", start)
					.setParameter("end", end)
					.getResultList();
	}

	@Override
	public List<TestSuiteRun> getFinishedTestSuiteRunsFromDateByStartTimeDesc(Date start) {
		return em.createNamedQuery("finishedTestSuiteRunsFromDateByStartTimeDesc", TestSuiteRun.class)
					.setParameter("start", start)
					.getResultList();
	}

	@Override
	public List<TestSuiteRun> getTestSuiteRunsForView1(long projectId,
			String build, String testSuite, List<Long> chosenHws) {
		return em.createNamedQuery("testSuiteRunsByProjectIdAndByBuildAndByTestSuiteAndByHwByStartTimeDesc", TestSuiteRun.class)
					.setParameter("projectId", projectId)
					.setParameter("build", build)
					.setParameter("testSuite", testSuite)
					.setParameter("chosenHws", chosenHws)
					.getResultList();
	}

	@Override
	public List<TestSuiteRun> getTestSuiteRunsByProjectIdAndByBuildAndByTestSuite(long projectId,
			String build, String testSuite) {
		return em.createNamedQuery("testSuiteRunsByProjectIdAndByBuildAndByTestSuiteByStartTimeDesc", TestSuiteRun.class)
					.setParameter("projectId", projectId)
					.setParameter("build", build)
					.setParameter("testSuite", testSuite)
					.getResultList();		
	}

	@Override
	public TestSuiteRun getFirstTestSuiteRunByProjectIdAndByTestSuite(
			long projectId, String testSuite) {
		List<TestSuiteRun> testSuiteRuns = em.createNamedQuery("testSuiteRunsByProjectIdAndByTestSuite", TestSuiteRun.class)
												.setParameter("projectId", projectId)
												.setParameter("testSuite", testSuite)
												.setMaxResults(2)
												.getResultList();
		
		return testSuiteRuns.isEmpty()? null : testSuiteRuns.get(0);
	}
}
