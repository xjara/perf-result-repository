package cz.vutbr.fit.mis.dip.perfserver.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
	@NamedQuery(name="attributeResultsOrderById", query="select r from Result r where r.attrResult.id = :attrResultId order by r.id"),
	@NamedQuery(name="getAvgResultByTestSuiteRunIdAndByMethodIdAndByAttributeId", query="select avg(r.value) from Result r, TestRun tr, AttrResult at where tr.testSuiteRun.id = :testSuiteRunId and tr.method.id = :methodId and tr.id = at.testRun.id and at.attr.id = :attributeId and at.id = r.attrResult.id"),
	@NamedQuery(name="getMinResultByTestSuiteRunIdAndByMethodIdAndByAttributeId", query="select min(r.value) from Result r, TestRun tr, AttrResult at where tr.testSuiteRun.id = :testSuiteRunId and tr.method.id = :methodId and tr.id = at.testRun.id and at.attr.id = :attributeId and at.id = r.attrResult.id"),
	@NamedQuery(name="getMaxResultByTestSuiteRunIdAndByMethodIdAndByAttributeId", query="select max(r.value) from Result r, TestRun tr, AttrResult at where tr.testSuiteRun.id = :testSuiteRunId and tr.method.id = :methodId and tr.id = at.testRun.id and at.attr.id = :attributeId and at.id = r.attrResult.id"),
	@NamedQuery(name="getAvgAttributeResultsValue", query="select avg(r.value) from Result r where r.attrResult.id = :attrResultId"),
	@NamedQuery(name="getMinAttributeResultsValue", query="select min(r.value) from Result r where r.attrResult.id = :attrResultId"),
	@NamedQuery(name="getMaxAttributeResultsValue", query="select max(r.value) from Result r where r.attrResult.id = :attrResultId")
})
public class Result implements Comparable<Result> {
	private Long id;
	private Date datetime;
	private Double value;
	
	private AttrResult attrResult;
	
	public Result() {
	}
	
	public Result(Date datetime, Double value, AttrResult attrResult) {
		this.datetime = datetime;
		this.value = value;
		this.attrResult = attrResult;
	}
	
	@Override
	public int compareTo(Result o) {
		double result = this.value - o.value;
		return result > 0? 1 : result < 0? -1 : 0;
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
	public Date getDatetime() {
		return datetime;
	}
	
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="attrresult_id")
	public AttrResult getAttrResult() {
		return attrResult;
	}
	
	public void setAttrResult(AttrResult attrResult) {
		this.attrResult = attrResult;
	}
}
