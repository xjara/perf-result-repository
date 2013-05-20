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
public class View3Form implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="TestSuite must be not empty")
	private String testSuite;
	@NotNull(message="Method must be not empty.")
	private Long methodId;
	@NotNull(message="Attribute must be not empty.")
	private Long attributeId;
	@NotNull(message="Function must be not empty.")
	private Fce function;
	@NotEmpty(message="At least one build must be chosen.")
	private String[] builds;

	public String getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(String testSuite) {
		this.testSuite = testSuite;
	}

	public Long getMethodId() {
		return methodId;
	}

	public void setMethodId(Long methodId) {
		this.methodId = methodId;
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
	
	public String[] getBuilds() {
		return builds; 
	}

	public void setBuilds(String[] builds) {
		this.builds = builds;
	}
	
	public List<String> getBuildsAsList() {
		return builds == null ? null : new LinkedList<String>(Arrays.asList(builds));
	}
}
