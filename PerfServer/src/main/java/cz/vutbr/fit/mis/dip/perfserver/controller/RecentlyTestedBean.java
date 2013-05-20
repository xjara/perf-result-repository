package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.enums.Const;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;


@ManagedBean
@ViewScoped
public class RecentlyTestedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private TestSuiteRunDao testSuiteRunDao;
	
	private List<TestSuiteRun> testSuiteRuns;
	private Date start;
	private Date end;
	
	@PostConstruct
	public void init() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -Const.DAYS_TO_PAST);
		start = cal.getTime();
		testSuiteRuns = testSuiteRunDao.getFinishedTestSuiteRunsFromDateByStartTimeDesc(start);
	}
	
	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public void setEnd(Date end) {
		this.end = end;
	}
	
	public List<TestSuiteRun> getTestSuiteRuns() {
		return testSuiteRuns;
	}
	
	public void setTestSuiteRuns(List<TestSuiteRun> testSuiteRuns) {
		this.testSuiteRuns = testSuiteRuns;
	}
	
	public void show() {
		if (start != null && end != null) {
			testSuiteRuns = testSuiteRunDao.getFinishedTestSuiteRunsInTimeRangeByStartTimeDesc(start, end);
		} else if (start != null) {
			testSuiteRuns = testSuiteRunDao.getFinishedTestSuiteRunsFromDateByStartTimeDesc(start);
		} else {
			testSuiteRuns = null;
		}
	}
}
