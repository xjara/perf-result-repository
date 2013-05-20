package cz.vutbr.fit.mis.dip.perfserver.controller.forms;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import cz.vutbr.fit.mis.dip.perfserver.enums.Fce;


@ManagedBean
@ViewScoped
public class View1Form implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="TestSuite must be not empty")
	private String testSuite;
	@NotEmpty(message="Build must be not empty.")
	private String build;
	@NotNull(message="Attribute must be not empty.")
	private Long attributeId;
	@NotNull(message="Function must be not empty.")
	private Fce function;
	@NotEmpty(message="At least one Platform must be chosen.")
	private Long[] hwIDs;

	public String getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(String testSuite) {
		this.testSuite = testSuite;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}
	
	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public Fce getFunction() {
		return function;
	}

	public void setFunction(Fce function) {
		this.function = function;
	}
	
	public Long[] getHwIDs() {
		return hwIDs; 
	}

	public void setHwIDs(Long[] hwIDs) {
		this.hwIDs = hwIDs;
	}
	
	public List<Long> getHwIDsAsList() {
		return hwIDs == null ? null : new LinkedList<Long>(Arrays.asList(hwIDs));
	}
}
