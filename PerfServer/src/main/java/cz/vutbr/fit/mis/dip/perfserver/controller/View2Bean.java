package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import cz.vutbr.fit.mis.dip.perfserver.controller.forms.View2Form;
import cz.vutbr.fit.mis.dip.perfserver.dao.BuildDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestRunDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;
import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;


@ManagedBean
@ViewScoped
public class View2Bean extends ViewBasicBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private BuildDao buildDao;
	@EJB
	private TestSuiteRunDao testSuiteRunDao;
	@EJB
	private TestRunDao testRunDao;
	@ManagedProperty(value="#{view2Form}")
	private View2Form form;
	
	private List<String> uniqueBuilds;
	private List<TestSuiteRun> baseTestSuiteRuns;
	private List<TestSuiteRun> comparedTestSuiteRuns;
	private List<TestRun> testRuns;	
	private List<Attr> chosenAttributes;
	private TestRun testSuite2TestRun;
	
	
	@PostConstruct
	public void init() {
		super.init();
		testRuns = null;
	}
	
	public void setForm(View2Form form) {
		this.form = form;
	}
	
	public List<String> getUniqueBuilds() {
		return uniqueBuilds;
	}
	
	public void handleTestSuiteChange() {
		uniqueBuilds = buildDao.getUniqueBuildNamesByProjectIdAndByTestSuite(projectId, form.getTestSuite());
	}
	
	public void handleBaseBuildChange() {
		baseTestSuiteRuns = testSuiteRunDao.getTestSuiteRunsByProjectIdAndByBuildAndByTestSuite(projectId, form.getBaseBuild(), form.getTestSuite());
	}
	
	public List<TestSuiteRun> getBaseTestSuiteRuns() {
		return baseTestSuiteRuns;
	}
	
	public void handleComparedBuildChange() {
		comparedTestSuiteRuns = testSuiteRunDao.getTestSuiteRunsByProjectIdAndByBuildAndByTestSuite(projectId, form.getComparedBuild(), form.getTestSuite()); 
	}
	
	public List<TestSuiteRun> getComparedTestSuiteRuns() {
		return comparedTestSuiteRuns;
	}
	
	public String refresh() {
		testRuns = testRunDao.getAllTestRuns(form.getBaseTestSuite());
		chosenAttributes = convertChosenAttrIDsToObjectList(form.getAttributeIDs());
		return null;
	}

	public List<TestRun> getTestRuns() {
		return testRuns;
	}
	
	private List<Attr> convertChosenAttrIDsToObjectList(Long[] attrIDs) {
		List<Attr> attrList = new LinkedList<Attr>();
		for(long attrId: attrIDs) {
			attrList.add(attributes.get(attributes.indexOf(new Attr(attrId))));
		}
		return attrList;
	}
	
	public List<Attr> getChosenAttributes() {
		return chosenAttributes;
	}
	
	public void determineTestSuite2TestRun(long methodId) {
		testSuite2TestRun = testRunDao.getTestRunByTestSuiteRunIdAndByMethodId(form.getComparedTestSuite(), methodId);
	}
	
	public TestRun getTestSuite2TestRun() {
		return testSuite2TestRun;
	}
}
