package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import cz.vutbr.fit.mis.dip.perfserver.dao.TestRunDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;




@ManagedBean
@ViewScoped
public class MethodBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@EJB
	private TestRunDao testRunDao;
	@EJB
	private TestSuiteRunDao testSuiteRunDao;
	
	private long testSuiteRunId;
	private TestSuiteRun testSuiteRun;
	private List<TestRun> testRuns; 
	
	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		testSuiteRunId = Long.parseLong(fc.getExternalContext().getRequestParameterMap().get("testsuiterunid"));
		testSuiteRun = testSuiteRunDao.getTestSuiteRunById(testSuiteRunId);
		testRuns = testRunDao.getAllTestRuns(testSuiteRunId);
	}
	
	public TestSuiteRun getTestSuiteRun() {
		return testSuiteRun;
	}
	
	public List<TestRun> getTestRuns() {
		return testRuns;
	}
}
