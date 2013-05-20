package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import cz.vutbr.fit.mis.dip.perfserver.controller.objects.ThresholdHelper;
import cz.vutbr.fit.mis.dip.perfserver.dao.ProjectDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ResultDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ThresholdDao;
import cz.vutbr.fit.mis.dip.perfserver.enums.Const;
import cz.vutbr.fit.mis.dip.perfserver.enums.Fce;
import cz.vutbr.fit.mis.dip.perfserver.enums.ThresholdStatus;
import cz.vutbr.fit.mis.dip.perfserver.model.Project;



@ManagedBean
@ViewScoped
public class ComputeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ResultDao resultDao;
	@EJB
	private ThresholdDao thresholdDao;
	@EJB
	private ProjectDao projectDao;
	
	private ThresholdHelper thresholdHelper;
	private Double baseTestSuiteValue = null;
	private Double comparedTestSuiteValue = null;
	private Double diff;
	private Double share;
	private DecimalFormat valueF;
	private DecimalFormat percentageF;
	
	public void init(Long projectId) {
		Project project = projectDao.getProjectById(projectId);
		thresholdHelper = new ThresholdHelper(thresholdDao.getGlobalThresholds(), thresholdDao.getProjectLocalThresholds(project.getName()));
	}
	
	private void initFormatting() {
		valueF = new DecimalFormat();
		valueF.setMaximumFractionDigits(Const.MAX_FRACTION_DIGITS);
		valueF.setGroupingUsed(false);
		percentageF = new DecimalFormat(Const.PERCENTAGE);
	}
	
	public Double getTestSuiteRunValueByAttributeAndFunction(Long testSuiteRunId, Long methodId, Long attributeId, Fce function) {
		Double value = null;
		if (function.equals(Fce.MEAN)) {
			value = resultDao.getAvgResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(testSuiteRunId, methodId, attributeId);
		} else if (function.equals(Fce.MIN)) {
			value = resultDao.getMinResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(testSuiteRunId, methodId, attributeId);		
		} else if (function.equals(Fce.MAX)) {
			value = resultDao.getMaxResultByTestSuiteRunIdAndByMethodIdAndByAttributeId(testSuiteRunId, methodId, attributeId);		
		}
		return value;
	}
	
	private Double determineTestSuiteValue(Long chosenSuiteId, long methodId, long attributeId, Fce function) {
		return chosenSuiteId == null? null : getTestSuiteRunValueByAttributeAndFunction(chosenSuiteId, methodId, attributeId, function);
	}
	
	public void setBaseTestSuiteValue(Long chosenSuiteId, long methodId, long attributeId, Fce function) {
		baseTestSuiteValue = determineTestSuiteValue(chosenSuiteId, methodId, attributeId, function);
	}
	
	public void setComparedTestSuiteValue(Long chosenSuiteId, long methodId, long attributeId, Fce function) {
		comparedTestSuiteValue = determineTestSuiteValue(chosenSuiteId, methodId, attributeId, function);
	}
	
	private Double absValue(Double value) {
		return value == null? null : Math.abs(value);
	}
	
	public Double formatValue(Double value) {
		return value == null? null : Double.valueOf(valueF.format(value));
	}
	
	public Double formatPercentage(Double value) {
		return value == null? null : Double.valueOf(percentageF.format(value));
	}
	
	public void computeDiffAndShare() {
		initFormatting();
		diff = (baseTestSuiteValue == null || comparedTestSuiteValue == null)? null : comparedTestSuiteValue - baseTestSuiteValue;
		Double absDiff = absValue(diff);
		share = absDiff == null? null : (baseTestSuiteValue == 0.0)? null : absDiff / baseTestSuiteValue * 100;
	}
	
	public Double getBaseTestSuiteValue() {
		return baseTestSuiteValue;
	}
	
	public Double getComparedTestSuiteValue() {
		return comparedTestSuiteValue;
	}
	
	public Double getDiff() {
		return diff;
	}
	
	public String getDiffAsText() {
		return diff == null? null : "(" + valueF.format(diff) + ")";
	}
	
	public Double getShare() {
		return share;
	}
	
	public String getShareAsText() {
		return share == null? null : percentageF.format(share) + " %"; 
	}
	
	public String determineStyleClass(String attribute) {
		ThresholdStatus thStatus = thresholdHelper.determineThresholdStatus(diff, attribute);
		return thStatus.getFlag();
	}
}
