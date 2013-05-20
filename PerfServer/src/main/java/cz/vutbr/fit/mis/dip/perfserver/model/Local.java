package cz.vutbr.fit.mis.dip.perfserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"project_id", "threshold_id"}))
@NamedQuery(name="localsByProjectId", query="from Local l where l.project.id = :projectId")
public class Local {
	private Long id;
	private Project project;
	private Threshold threshold;
	
	public Local() {
	}
	
	public Local(Project project, Threshold threshold) {
		this.project = project;
		this.threshold = threshold;
	}
	
	public Local(Threshold threshold) {
		this.threshold = threshold;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="project_id")
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	@ManyToOne
	@JoinColumn(name="threshold_id")
	public Threshold getThreshold() {
		return threshold;
	}
	
	public void setThreshold(Threshold threshold) {
		this.threshold = threshold;
	}
}
