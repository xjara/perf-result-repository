package cz.vutbr.fit.mis.dip.perfserver.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@NamedQueries({
	@NamedQuery(name="testSuiteRunsByProjectIdByStartTimeDesc", query="select ts from TestSuiteRun ts, TestSuite t, Build b, Project p where p.id = :projectId and p.id = b.project.id and b.id = t.build.id and t.id = ts.testSuite.id order by ts.startTime desc"),
	@NamedQuery(name="lastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild", query="select tr from TestSuiteRun tr, TestSuite t, Project p, Build b where p.name = :project and p.id = b.project.id and b.name = :build and b.id = t.build.id and t.name = :testSuite and t.id = tr.testSuite.id and tr.endTime != null order by tr.startTime desc"),
	@NamedQuery(name="actuallyTestedTestSuiteRunsByStartTimeDesc", query="select ts from TestSuiteRun ts, TestSuite t, Build b, Project p where p.id = b.project.id and b.id = t.build.id and t.id = ts.testSuite.id and ts.endTime = null order by ts.startTime desc"),
	@NamedQuery(name="finishedTestSuiteRunsInTimeRangeByStartTimeDesc", query="select ts from TestSuiteRun ts where ts.endTime != null and ts.endTime >= :start and ts.endTime <= :end order by ts.startTime desc"),
	@NamedQuery(name="finishedTestSuiteRunsFromDateByStartTimeDesc", query="select ts from TestSuiteRun ts where ts.endTime != null and ts.endTime >= :start order by ts.startTime desc"),
	@NamedQuery(name="testSuiteRunsByProjectIdAndByBuildAndByTestSuiteAndByHwByStartTimeDesc", query="select tr from TestSuiteRun tr, TestSuite t, Project p, Build b where p.id = :projectId and p.id = b.project.id and b.name = :build and b.id = t.build.id and t.name = :testSuite and tr.testSuite.id = t.id and tr.hw.id in :chosenHws order by tr.hw.id, tr.startTime desc"),
	@NamedQuery(name="testSuiteRunsByProjectIdAndByBuildAndByTestSuiteByStartTimeDesc", query="select tr from TestSuiteRun tr, TestSuite t, Project p, Build b where p.id = :projectId and p.id = b.project.id and b.name = :build and b.id = t.build.id and t.name = :testSuite and tr.testSuite.id = t.id order by tr.startTime desc"),
	@NamedQuery(name="testSuiteRunsByProjectIdAndByTestSuite", query="select tr from TestSuiteRun tr, TestSuite t, Project p, Build b where p.id = :projectId and p.id = b.project.id and b.id = t.build.id and t.name = :testSuite and tr.testSuite.id = t.id")
})
public class TestSuiteRun {
	private Long id;
	private Date startTime;
	private Date endTime;
	
	private Hw hw;
	private TestSuite testSuite;
	private List<TestRun> testRuns = new LinkedList<TestRun>();
	
	public TestSuiteRun() {
	}
	
	public TestSuiteRun(Date startTime, Hw hw, TestSuite testSuite) {
		this.startTime = startTime;
		this.hw = hw;
		this.testSuite = testSuite;
	}
	
	public TestSuiteRun(Date startTime, Date endTime, Hw hw, TestSuite testSuite) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.hw = hw;
		this.testSuite = testSuite;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndTime() {
		return endTime;
	}
		
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@OneToOne(cascade=CascadeType.DETACH, fetch=FetchType.EAGER)
	@JoinColumn(name="hw_id")
	public Hw getHw() {
		return hw;
	}
	
	public void setHw(Hw hw) {
		this.hw = hw;
	}

	@ManyToOne
	@JoinColumn(name="testsuite_id")
	public TestSuite getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(TestSuite testSuite) {
		this.testSuite = testSuite;
	}
	
	@OneToMany(targetEntity=TestRun.class, mappedBy="testSuiteRun", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public List<TestRun> getTestRuns() {
		return testRuns;
	}

	public void setTestRuns(List<TestRun> testRuns) {
		this.testRuns = testRuns;
	}
	
	public void addTestRun(TestRun testRun) {
		this.testRuns.add(testRun);
	}
}

