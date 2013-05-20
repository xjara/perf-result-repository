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
public class View2Form implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="TestSuite name must be not empty")
	private String testSuite;
	@NotEmpty(message="Base build must be not empty.")
	private String baseBuild;
	@NotEmpty(message="Compared build must be not empty.")
	private String comparedBuild;
	@NotNull(message="Base TestSuite run must be not empty.")
	private Long baseTestSuite;
	@NotNull(message="Compared TestSuite run must be not empty.")
	private Long comparedTestSuite;
	@NotEmpty(message="At least one attribute must be chosen.")
	private Long[] attributeIDs;
	@NotNull(message="Function must be not empty.")
	private Fce function;
	
	public String getTestSuite() {
		return testSuite;
	}
	
	public void setTestSuite(String testSuite) {
		this.testSuite = testSuite;
	}
	
	public String getBaseBuild() {
		return baseBuild;
	}

	public void setBaseBuild(String baseBuild) {
		this.baseBuild = baseBuild;
	}

	public String getComparedBuild() {
		return comparedBuild;
	}

	public void setComparedBuild(String comparedBuild) {
		this.comparedBuild = comparedBuild;
	}

	public Long getBaseTestSuite() {
		return baseTestSuite;
	}

	public void setBaseTestSuite(Long baseTestSuite) {
		this.baseTestSuite = baseTestSuite;
	}

	public Long getComparedTestSuite() {
		return comparedTestSuite;
	}

	public void setComparedTestSuite(Long comparedTestSuite) {
		this.comparedTestSuite = comparedTestSuite;
	}

	public Long[] getAttributeIDs() {
		return attributeIDs;
	}

	public void setAttributeIDs(Long[] attributeIDs) {
		this.attributeIDs = attributeIDs;
	}
	
	public List<Long> getAttributesAsList() {
		return attributeIDs == null ? null : new LinkedList<Long>(Arrays.asList(attributeIDs));
	}
	
	public Fce getFunction() {
		return function;
	}

	public void setFunction(Fce function) {
		this.function = function;
	}
}
