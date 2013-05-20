package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import cz.vutbr.fit.mis.dip.perfserver.dao.TestRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;


@ManagedBean
@RequestScoped
public class TestRunBean {
	@EJB
	private TestRunDao testRunDao;
	
	public List<TestRun> getSuccessfullTestRuns(long testSuiteRunId) {
		return testRunDao.getSuccessfullTestRuns(testSuiteRunId);
	}
	
	public List<TestRun> getFailedTestRuns(long testSuiteRunId) {
		return testRunDao.getFailedTestRuns(testSuiteRunId);
	}
}
