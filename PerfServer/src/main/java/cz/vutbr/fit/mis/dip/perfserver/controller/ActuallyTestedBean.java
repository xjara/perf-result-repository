package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;


@ManagedBean
@ViewScoped
public class ActuallyTestedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private TestSuiteRunDao testSuiteRunDao;
	private List<TestSuiteRun> testSuiteRuns;
	
	@PostConstruct
	public void init() {
		testSuiteRuns = testSuiteRunDao.getActuallyTestedTestSuiteRunsByStartTimeDesc();
	}

	public List<TestSuiteRun> getTestSuiteRuns() {
		return testSuiteRuns;
	}
}
