package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import cz.vutbr.fit.mis.dip.perfserver.controller.forms.View1Form;
import cz.vutbr.fit.mis.dip.perfserver.controller.objects.TestSuiteValue;
import cz.vutbr.fit.mis.dip.perfserver.dao.BuildDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.HwDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestRunDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;
import cz.vutbr.fit.mis.dip.perfserver.model.Hw;
import cz.vutbr.fit.mis.dip.perfserver.model.Method;
import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;



@ManagedBean
@ViewScoped
public class View1Bean extends ViewBasicBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private BuildDao buildDao;
	@EJB
	private HwDao hwDao;
	@EJB
	private TestSuiteRunDao testSuiteRunDao;
	@EJB
	private TestRunDao testRunDao;
	@Inject
	private ComputeBean computeBean;
	@ManagedProperty(value="#{view1Form}")
	private View1Form form;
	
	private List<TestSuiteRun> testSuiteRuns;
	private List<TestSuiteValue> testSuiteValues;
	private List<String> builds;
	private List<Hw> hws;
	private Attr attr;
	private CartesianChartModel chart = null;
	
	
	@PostConstruct
	public void init() {
		super.init();
	}
	
	public void setForm(View1Form form) {
		this.form = form;
	}
	
	public void handleTestSuiteChange() {
		builds = buildDao.getUniqueBuildNamesByProjectIdAndByTestSuite(projectId, form.getTestSuite());
	}
	
	public void handleBuildChange() {
		hws = hwDao.getUniqueHwsByProjectIdAndByBuildAndByTestSuite(projectId, form.getBuild(), form.getTestSuite());
	}
	
	public List<String> getProjectBuilds() {
		return builds;
	}
	
	public List<Hw> getHws() {
		return hws;
	}
	
	private Attr getAttrObject(long attrId) {
		return attributes == null ? null : attributes.get(attributes.indexOf(new Attr(attrId)));
	}
	
	public String refresh() {
		attr = getAttrObject(form.getAttributeId());
		return null;
	}
	
	public Attr getAttr() {
		return attr;
	}
	
	public List<TestRun> getTestRuns() {
		if (form.getBuild() == null || form.getHwIDsAsList() == null) {
			return null;
		}
		
		testSuiteRuns = testSuiteRunDao.getTestSuiteRunsForView1(projectId, form.getBuild(), form.getTestSuite(), form.getHwIDsAsList());		
		// all "methods" which will be used for comparison are taken from the first TestSuiteRun
		return testSuiteRuns.isEmpty()? null : testSuiteRuns.get(0).getTestRuns();
	}
	
	public void createChart(Method method) {
		chart = new CartesianChartModel();
		testSuiteValues = new LinkedList<TestSuiteValue>();
		int index = 1;
		for(TestSuiteRun testSuiteRun: testSuiteRuns) {
			Double value = computeBean.getTestSuiteRunValueByAttributeAndFunction(testSuiteRun.getId(), method.getId(), form.getAttributeId(), form.getFunction());
			ChartSeries s = new ChartSeries(index + "");
			testSuiteValues.add(new TestSuiteValue(index, testSuiteRun.getStartTime(), testSuiteRun.getEndTime() == null? false : true, testSuiteRun.getHw().getName(), value));
			s.set("TestSuites", value);
			chart.addSeries(s);
			index++;
		}	
	}
	
	public CartesianChartModel getChart() {
		return chart;
	}
	
	public List<TestSuiteValue> getTestSuiteValues() {
		return testSuiteValues;
	}
	
	public void itemSelect(ItemSelectEvent event) throws IOException {
		String chosenMethod = ((BarChart) event.getSource()).getTitle();
		TestRun testRun = testRunDao.getTestRunByTestSuiteRunIdAndByMethod(testSuiteRuns.get(event.getSeriesIndex()).getId(), chosenMethod);
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/faces/results.xhtml?testrunid=" + testRun.getId() + "&attributeid=" + form.getAttributeId());
    }
}
