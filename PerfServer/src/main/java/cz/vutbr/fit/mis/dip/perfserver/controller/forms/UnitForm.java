package cz.vutbr.fit.mis.dip.perfserver.controller.forms;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ManagedBean
@ViewScoped
public class UnitForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Size(max=255)
	@Pattern(regexp="[a-zA-Z_][a-zA-Z0-9_]*", message="Unit must fulfill identifier format.")
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void clear() {
		name = null;
	}
}
