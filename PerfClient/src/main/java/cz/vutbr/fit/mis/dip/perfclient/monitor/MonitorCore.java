package cz.vutbr.fit.mis.dip.perfclient.monitor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import cz.vutbr.fit.mis.dip.perfclient.communicator.Communicator;
import cz.vutbr.fit.mis.dip.perfclient.communicator.Method;
import cz.vutbr.fit.mis.dip.perfclient.config.BaseConfig;
import cz.vutbr.fit.mis.dip.perfobjects.constants.Format;
import cz.vutbr.fit.mis.dip.perfobjects.constants.RelURL;
import cz.vutbr.fit.mis.dip.perfobjects.objects.AttrResultData;
import cz.vutbr.fit.mis.dip.perfobjects.objects.Attribute;
import cz.vutbr.fit.mis.dip.perfobjects.objects.LastTestRunData;
import cz.vutbr.fit.mis.dip.perfobjects.objects.Result;

public class MonitorCore {
	private static final Logger logger = LoggerFactory.getLogger(MonitorCore.class);
	private static MonitorCore core;
	private Communicator communicator;
	private BaseConfig config;
	private Long testRunId;
	private Long testSuiteRunId;
	private final Gson gson;
	private final SimpleDateFormat sdf;
	
	private AttrResultData attrResultData;	// buffer
	
	public MonitorCore() {
		communicator = new Communicator();
		gson = new Gson();
		sdf = new SimpleDateFormat(Format.DATE_TIME);
		attrResultData = new AttrResultData();
	}
	
	public static synchronized MonitorCore getInstance() {
		if (core == null) {
			core = new MonitorCore();
		}
		return core;
	}
	
	public MonitorCore initialize(BaseConfig config) {
		this.config = config;
		return this;
	}
	
	public BaseConfig getConfig() {
		return config;
	}

	public Communicator getCommunicator() {
		return communicator;
	}

	public Long getTestRunId() {
		return testRunId;
	}

	public void setTestRunId(Long testRunId) {
		this.testRunId = testRunId;
	}

	public Long getTestSuiteRunId() {
		return testSuiteRunId;
	}

	public void setTestSuiteRunId(Long testSuiteRunId) {
		this.testSuiteRunId = testSuiteRunId;
	}

	public Gson getGson() {
		return gson;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public AttrResultData getAttrResultData() {
		return attrResultData;
	}

	public void setAttrResultData(AttrResultData attrResultData) {
		this.attrResultData = attrResultData;
	}
	
	public void addMeasuredAttributeToAttrResultData(String att, String unit, Double value) {
		Attribute attribute = new Attribute(unit);
		attribute.getResults().add(new Result(sdf.format(new Date()), value)); 
		attrResultData.getAttrResult().put(att, attribute);
	}
	
	public void sendLastTestRunData(String description, boolean success, String error, String parameters) {
		LastTestRunData lastTestRunData = new LastTestRunData(description, success, error, parameters);
		String data = gson.toJson(lastTestRunData, LastTestRunData.class);	
		communicator.setUpConnection(config.getRepUrl() + RelURL.TEST_RUN_FINISH + testRunId, Method.POST);
		logger.debug("SEND LAST METHOD DATA");
		// answer is always null
		communicator.sendDataAndReceiveAnswer(data);
	}
}
