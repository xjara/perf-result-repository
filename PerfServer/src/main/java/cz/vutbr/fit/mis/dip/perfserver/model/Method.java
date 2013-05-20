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
	@NamedQuery(name="methodByName", query="from Method m where m.name = :method"),
	@NamedQuery(name="methodById", query="from Method m where m.id = :methodId"),
	@NamedQuery(name="allMethodsByTestSuiteRunId", query="select m from Method m, TestSuiteRun ts, TestRun tr where ts.id = :testSuiteRunId and ts.id = tr.testSuiteRun.id and tr.method.id = m.id"),
})
public class Method {
	private Long id;
	private String name;
	
	public Method() {
	}
	
	public Method(String name) {
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
}
