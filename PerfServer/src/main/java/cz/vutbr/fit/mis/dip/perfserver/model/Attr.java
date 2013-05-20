package cz.vutbr.fit.mis.dip.perfserver.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({
	@NamedQuery(name="attrs", query="from Attr"),
	@NamedQuery(name="attrsById", query="from Attr a where a.id = :attrId"),
	@NamedQuery(name="attrsByName", query="from Attr a where a.name = :attr"),
	@NamedQuery(name="attrsByUnit", query="select a from Attr a, Unit u where u.name = :unit and a.unit.id = u.id")
})
public class Attr {	
	private Long id;
	private String name;
	
	private Unit unit;

	public Attr() {
	}
	
	public Attr(Long id) {
		this.id = id;
	}
	
	public Attr(String name, Unit unit) {
		this.name = name;
		this.unit = unit;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique=true, length=255, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToOne(cascade=CascadeType.DETACH, fetch=FetchType.EAGER)
	@JoinColumn(name="unit_id")
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Attr) {
			if (this.id == ((Attr) o).getId()) {
				return true;
			}
		}
		return false;
	}
}
