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
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"name", "project_id"}))
@NamedQueries({
	@NamedQuery(name="buildsByProjectId", query="select b from Project p, Build b where p.id = :projectId and p.id = b.project.id"),
	@NamedQuery(name="buildByNameAndByProjectId", query="select b from Build b, Project p where b.name = :build and b.project.id = :projectId"),
	@NamedQuery(name="buildByNameAndByProject", query="select b from Build b, Project p where b.name = :build and b.project.id = p.id and p.name = :project"),
	@NamedQuery(name="uniqueBuildNamesByProjectIdAndByTestSuite", query="select distinct b.name from Build b, Project p, TestSuite t where p.id = :projectId and p.id = b.project.id and b.id = t.build.id and t.name = :testSuite")
})
public class Build {
	private Long id;
	private String name;
	
	private Project project;
	private List<TestSuite> testSuites = new LinkedList<TestSuite>();
	
	public Build() {
	}
	
	public Build(String name, Project project) {
		this.name = name;
		this.project = project;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length=1024, nullable=false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name="project_id")
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	@OneToMany(targetEntity=TestSuite.class, mappedBy="build", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<TestSuite> getTestSuites() {
		return testSuites;
	}
	
	public void setTestSuites(List<TestSuite> testSuites) {
		this.testSuites = testSuites;
	}
}
