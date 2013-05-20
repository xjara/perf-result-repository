package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import cz.vutbr.fit.mis.dip.perfserver.controller.forms.View3Form;
import cz.vutbr.fit.mis.dip.perfserver.controller.objects.BuildTestSuiteValue;
import cz.vutbr.fit.mis.dip.perfserver.dao.BuildDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.MethodDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestRunDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;
import cz.vutbr.fit.mis.dip.perfserver.model.Method;
import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;

@ManagedBean
@ViewScoped
public class View3Bean extends ViewBasicBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private BuildDao buildDao;
	@EJB
	private TestSuiteRunDao testSuiteRunDao;
	@EJB
	private TestRunDao testRunDao;
	@EJB
	private MethodDao methodDao;
	@Inject
	private ComputeBean computeBean;
	@ManagedProperty(value="#{view3Form}")
	private View3Form form;
	
	private Attr attr;
	private Collection<TestRun> methods;
	private Method method;
	private List<String> builds;
	private List<TestRun> testRuns;
	private List<BuildTestSuiteValue> buildTestSuiteValues;
	private CartesianChartModel chart = null;
	
	
	@PostConstruct
	public void init() {
		super.init();
	}
	
	public void setForm(View3Form form) {
		this.form = form;
	}
	
	private Set<TestRun> createListOfUniqueMethods(TestSuiteRun testSuiteRun) {
		Set<TestRun> uniqueTestRuns = new TreeSet<TestRun>(testSuiteRun.getTestRuns());
		return uniqueTestRuns;
	}
	
	public void handleTestSuiteChange() {
		TestSuiteRun testSuiteRun = testSuiteRunDao.getFirstTestSuiteRunByProjectIdAndByTestSuite(projectId, form.getTestSuite());
		methods = createListOfUniqueMethods(testSuiteRun);
		builds = buildDao.getUniqueBuildNamesByProjectIdAndByTestSuite(projectId, form.getTestSuite());
	}
	
	public Collection<TestRun> getMethods() {
		return methods;
	}
	
	public List<String> getBuilds() {
		return builds;
	}
	
	public List<TestRun> getTestRuns() {
		return testRuns;
	}
	
	private Attr getAttrObject(long attrId) {
		return attributes == null ? null : attributes.get(attributes.indexOf(new Attr(attrId)));
	}
	
	public String refresh() {
		testRuns = testRunDao.getTestRunsForView3(projectId, form.getBuildsAsList(), form.getTestSuite(), form.getMethodId());
		method = methodDao.getMethodById(form.getMethodId());
		attr = getAttrObject(form.getAttributeId());
		return null;
	}	
	
	public Method getMethod() {
		return method;
	}
	
	public Attr getAttr() {
		return attr;
	}
	
	public void createChart() {
		if (testRuns == null) {
			return;
		}
		chart = new CartesianChartModel();
		buildTestSuiteValues = new LinkedList<BuildTestSuiteValue>();
		int index = 1;
		for(TestRun testRun: testRuns) {
			Double value = computeBean.getTestSuiteRunValueByAttributeAndFunction(testRun.getTestSuiteRun().getId(), form.getMethodId(), form.getAttributeId(), form.getFunction());
			ChartSeries s = new ChartSeries(index + "");
			buildTestSuiteValues.add(new BuildTestSuiteValue(index, testRun.getTestSuiteRun().getTestSuite().getBuild().getName(), testRun.getTestSuiteRun().getStartTime(), testRun.getTestSuiteRun().getEndTime() == null? false : true, testRun.getTestSuiteRun().getHw().getName(), value));
			s.set("TestSuites", value);
			chart.addSeries(s);
			index++;
		}	
	}
	
	public CartesianChartModel getChart() {
		return chart;
	}
	
	public List<BuildTestSuiteValue> getBuildTestSuiteValues() {
		return buildTestSuiteValues;
	}
	
	public void itemSelect(ItemSelectEvent event) throws IOException {
		Long testRunId = testRuns.get(event.getSeriesIndex()).getId();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/faces/results.xhtml?testrunid=" + testRunId + "&attributeid=" + form.getAttributeId());
    }
}
