package cz.vutbr.fit.mis.dip.perfserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="hws", query="from Hw h"),
	@NamedQuery(name="hwsByName", query="from Hw h where h.name = :hw"),
	@NamedQuery(name="hwsByProjectIdAndByBuildAndByTestSuite", query="select h from Hw h, Project p, Build b, TestSuite t, TestSuiteRun tr where p.id = :projectId and p.id = b.project.id and b.name = :build and b.id = t.build.id and t.name = :testSuite and tr.testSuite.id = t.id and tr.hw.id = h.id")
})
public class Hw {
	private Long id;
	private String name;

	public Hw() {
	}
	
	public Hw(String name) {
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

	@Column(unique=true, nullable=false, length=1024)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Hw) {
			if (this.name.equals(((Hw) object).name)) {
				return true;
			}
		}
		return false;
	}
}
