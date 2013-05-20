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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
	@NamedQuery(name="projects", query="from Project"),
	@NamedQuery(name="projectsByName", query="from Project p where p.name = :project")	
})
public class Project {
	private Long id;
	private String name;

	private List<Build> builds = new LinkedList<Build>();
	private List<Local> locals = new LinkedList<Local>();
	
	public Project() {
	}
	
	public Project(String name) {
		this.name = name;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique=true, length=1024, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(targetEntity=Build.class, mappedBy="project", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<Build> getBuilds() {
		return builds;
	}

	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}

	@OneToMany(targetEntity=Local.class, mappedBy="project", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<Local> getLocals() {
		return locals;
	}

	public void setLocals(List<Local> locals) {
		this.locals = locals;
	}
	
	public void addLocal(Local local) {
		locals.add(local);
	}
}
