package cz.vutbr.fit.mis.dip.perfserver.controller.forms;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@ViewScoped
public class ThresholdForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="Attribute must be not empty.")
	private String attribute;
	// for restriction is used DoubleValueValidator
	private String value;
	
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void clear() {
		attribute = null;
		value = null;
	}
}
