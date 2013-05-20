package cz.vutbr.fit.mis.dip.perfserver.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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


@Entity
@NamedQueries({
	@NamedQuery(name="allTestRunsByTestSuiteRunId", query="select t from TestRun t where t.testSuiteRun.id = :testSuiteRunId"),
	@NamedQuery(name="successfullTestRunsByTestSuiteRunId", query="select t from TestRun t where t.testSuiteRun.id = :testSuiteRunId and t.status = 1"),
	@NamedQuery(name="failedTestRunsByTestSuiteRunId", query="select t from TestRun t where t.testSuiteRun.id = :testSuiteRunId and t.status = 0"),
	@NamedQuery(name="testRunsByTestSuiteRunIdAndByMethod", query="select t from TestRun t, TestSuiteRun ts where ts.id = :testSuiteRunId and t.testSuiteRun.id = ts.id and t.method.name = :method"),
	@NamedQuery(name="testRunsByTestSuiteRunIdAndByMethodId", query="select t from TestRun t, TestSuiteRun ts where ts.id = :testSuiteRunId and t.testSuiteRun.id = ts.id and t.method.id = :methodId"),
	@NamedQuery(name="testedTestRunByTestSuiteRunId", query="select t from TestRun t, TestSuiteRun ts where ts.id = :testSuiteRunId and t.testSuiteRun.id = ts.id and t.status = 3"),
	@NamedQuery(name="testRunsByProjectIdAndByBuildsAndByTestSuiteAndByMethodIdByStartTimeDesc", query="select tr from TestRun tr, Build b, TestSuite ts, TestSuiteRun tsr where b.project.id = :projectId and b.name in (:builds) and ts.build.id = b.id and ts.name = :testSuite and ts.id = tsr.testSuite.id and tsr.id = tr.testSuiteRun.id and tr.method.id = :methodId order by tsr.startTime desc")
})
public class TestRun implements Comparable<TestRun> {
	private Long id;
	private Integer status;
	private String error;
	private String parameters;
	private String description;
	
	private Method method;
	private TestSuiteRun testSuiteRun;
	private List<AttrResult> attrResults = new LinkedList<AttrResult>();
	
	public TestRun() {
	}
	
	public TestRun(Integer status, Method method, TestSuiteRun testSuiteRun) {
		this.status = status;
		this.method = method;
		this.testSuiteRun = testSuiteRun;
	}
	
	public TestRun(Integer status, String error, String parameters, Method method) {
		this.status = status;
		this.error = error;
		this.parameters = parameters;
		this.method = method;
	}
	
	public TestRun(Integer status, String error, String parameters, String description, Method method, TestSuiteRun testSuiteRun) {
		this.status = status;
		this.error = error;
		this.parameters = parameters;
		this.description = description;
		this.method = method;
		this.testSuiteRun = testSuiteRun;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(length=5000)
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	@Column(length=1024)
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	@Column(length=1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToOne(cascade=CascadeType.DETACH, fetch=FetchType.EAGER)
	@JoinColumn(name="method_id")
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@ManyToOne
	@JoinColumn(name="testsuiterun_id")
	public TestSuiteRun getTestSuiteRun() {
		return testSuiteRun;
	}
	
	public void setTestSuiteRun(TestSuiteRun testSuiteRun) {
		this.testSuiteRun = testSuiteRun;
	}
	
	@OneToMany(targetEntity=AttrResult.class, mappedBy="testRun", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<AttrResult> getAttrResults() {
		return attrResults;
	}
	
	public void setAttrResults(List<AttrResult> attrResults) {
		this.attrResults = attrResults;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TestRun) {
			if (this.method.getName().equals(((TestRun) o).method.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(TestRun o) {
		return this.method.getName().compareTo(o.method.getName());
	} 
}
