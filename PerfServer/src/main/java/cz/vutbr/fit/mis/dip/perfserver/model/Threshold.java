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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({
	@NamedQuery(name="globalThresholds", query="select t from Threshold t, Global g where t.id = g.threshold.id"),
	@NamedQuery(name="globalThresholdsByAttr", query="select t from Threshold t, Attr a, Global g where a.name = :attr and a.id = t.attr.id and t.id = g.threshold.id"),
	@NamedQuery(name="globalThresholdsByUnitId", query="select t from Threshold t, Global g, Attr a where t.id = g.threshold.id and t.attr.id = a.id and a.unit.id = :unitId"),
	@NamedQuery(name="globalThresholdsByAttrId", query="select t from Threshold t, Global g where t.attr.id = :attrId and t.id = g.threshold.id"),
	@NamedQuery(name="localThresholdsByUnitId", query="select t from Threshold t, Local l, Attr a where l.threshold.id = t.id and t.attr.id = a.id and a.unit.id = :unitId"),
	@NamedQuery(name="localThresholdsByAttrId", query="select t from Threshold t, Local l, Attr a where l.threshold.id = t.id and t.attr.id = :attrId"),
	@NamedQuery(name="localThresholdsByProjectAndByAttr", query="select t from Threshold t, Project p, Local l where p.name = :project and l.project.id = p.id and l.threshold.id = t.id and t.attr.name = :attr"),
	@NamedQuery(name="localThresholdsByProject", query="select t from Project p, Local l, Threshold t where p.name = :project and p.id = l.project.id and l.threshold.id = t.id"),
	@NamedQuery(name="localThresholdsByProjectIdAndByAttr", query="select t from Threshold t, Local l where l.project.id = :projectId and l.threshold.id = t.id and t.attr.name = :attr")
})
public class Threshold {
	private Long id;
	private Double value;
	
	private Attr attr;
	private List<Global> globals = new LinkedList<Global>();
	private List<Local> locals = new LinkedList<Local>();
	
	public Threshold() {
	}
	
	public Threshold(double value, Attr attr) {
		this.value = value;
		this.attr = attr;
	}
	
	public void addGlobal(Global global) {
		globals.add(global);
	}
	
	public void addLocal(Local local) {
		locals.add(local);
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	@OneToOne(cascade=CascadeType.DETACH, fetch=FetchType.EAGER)
	@JoinColumn(name="attr_id")
	public Attr getAttr() {
		return attr;
	}
	
	public void setAttr(Attr attr) {
		this.attr = attr;
	}
	
	@OneToMany(targetEntity=Global.class, mappedBy="threshold", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public List<Global> getGlobals() {
		return globals;
	}
	
	public void setGlobals(List<Global> globals) {
		this.globals = globals;
	}
	
	@OneToMany(targetEntity=Local.class, mappedBy="threshold", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<Local> getLocals() {
		return locals;
	}
	
	public void setLocals(List<Local> locals) {
		this.locals = locals;
	}
}
