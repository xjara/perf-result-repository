package cz.vutbr.fit.mis.dip.perfserver.model;

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

@Entity
@NamedQueries({
	@NamedQuery(name="attrResultByTestRunId", query="from AttrResult a where a.testRun.id = :testRunId"),
	@NamedQuery(name="attrResultByAttrId", query="select ar from AttrResult ar, Attr a where a.id = :attrId and a.id = ar.attr.id"),
	@NamedQuery(name="attrResultByUnitId", query="select ar from AttrResult ar, Attr a, Unit u where ar.attr.id = a.id and a.unit.id = :unitId"),
	@NamedQuery(name="attrResultByMethodId", query="select a from AttrResult a, TestRun t where t.method.id = :methodId and t.id = a.testRun.id"),
	@NamedQuery(name="attrResultByTestRunIdAndByAttrId", query="from AttrResult a where a.testRun.id = :testRunId and a.attr.id = :attrId")
})
public class AttrResult {
	private Long id;	
	private Attr attr;
	private TestRun testRun;
	private List<Result> results = new LinkedList<Result>();
	
	public AttrResult() {
	}
	
	public AttrResult(Attr attr, TestRun testRun) {
		this.attr = attr;
		this.testRun = testRun;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(cascade=CascadeType.DETACH, fetch=FetchType.EAGER)
	@JoinColumn(name="attr_id")
	public Attr getAttr() {
		return attr;
	}
	
	public void setAttr(Attr attr) {
		this.attr = attr;
	}
	
	@ManyToOne
	@JoinColumn(name="testrun_id")
	public TestRun getTestRun() {
		return testRun;
	}
	
	public void setTestRun(TestRun testRun) {
		this.testRun = testRun;
	}
	
	@OneToMany(targetEntity=Result.class, mappedBy="attrResult", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public List<Result> getResults() {
		return results;
	}
	
	public void setResults(List<Result> results) {
		this.results = results;
	}
}
