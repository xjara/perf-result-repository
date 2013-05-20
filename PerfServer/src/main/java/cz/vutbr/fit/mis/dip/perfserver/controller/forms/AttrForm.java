package cz.vutbr.fit.mis.dip.perfserver.controller.forms;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@ViewScoped
public class AttrForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Size(max=255)
	@Pattern(regexp="[a-zA-Z_][a-zA-Z0-9_]*", message="Attribute must fulfill identifier format.")
	private String name;
	@NotEmpty(message="Unit must be not empty.")
	private String unit;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public void clear() {
		name = null;
		unit = null;
	}
}
