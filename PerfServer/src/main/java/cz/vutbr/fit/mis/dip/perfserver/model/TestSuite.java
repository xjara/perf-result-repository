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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"name", "build_id"}))
@NamedQueries({
	@NamedQuery(name="testSuiteByNameAndByProjectAndByBuild", query="select t from TestSuite t, Project p, Build b where p.name = :project and p.id = b.project.id and b.name = :build and b.id = t.build.id and t.name = :testSuite"),
	@NamedQuery(name="testSuiteByNameAndByBuildId", query="select t from TestSuite t, Build b where b.id = :buildId and b.id = t.build.id and t.name = :testSuite"),
	@NamedQuery(name="uniqueTestSuiteNamesByProjectId", query="select distinct t.name from TestSuite t, Build b where b.project.id = :projectId and b.id = t.build.id"),
})
public class TestSuite { 	
	private Long id;
	private String name;

	private Build build;
	private List<TestSuiteRun> testSuiteRuns = new LinkedList<TestSuiteRun>();
	
	public TestSuite() {
	}
	
	public TestSuite(String name, Build build) {
		this.name = name;
		this.build = build;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length=1024)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name="build_id")
	public Build getBuild() {
		return build;
	}
	
	public void setBuild(Build build) {
		this.build = build;
	}

	@OneToMany(targetEntity=TestSuiteRun.class, mappedBy="testSuite", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<TestSuiteRun> getTestSuiteRuns() {
		return testSuiteRuns;
	}
	
	public void setTestSuiteRuns(List<TestSuiteRun> testSuiteRuns) {
		this.testSuiteRuns = testSuiteRuns;
	}
}
