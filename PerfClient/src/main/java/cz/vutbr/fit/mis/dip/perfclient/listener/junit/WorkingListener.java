package cz.vutbr.fit.mis.dip.perfclient.listener.junit;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hyperic.sigar.SigarException;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.fit.mis.dip.perfclient.config.BaseConfig;
import cz.vutbr.fit.mis.dip.perfclient.constants.A;
import cz.vutbr.fit.mis.dip.perfclient.communicator.Method;
import cz.vutbr.fit.mis.dip.perfclient.constants.U;
import cz.vutbr.fit.mis.dip.perfclient.monitor.MonitorCore;
import cz.vutbr.fit.mis.dip.perfclient.monitor.PerfMonitor;
import cz.vutbr.fit.mis.dip.perfobjects.constants.RelURL;
import cz.vutbr.fit.mis.dip.perfobjects.objects.LastTestSuiteData;
import cz.vutbr.fit.mis.dip.perfobjects.objects.TestSuiteData;

public class WorkingListener extends RunListener {
	private static final Logger logger = LoggerFactory.getLogger(WorkingListener.class);
	private PerfMonitor perfMonitor;
	private MonitorCore core;
	private String prevMethodName;
	private int methodRun;
	private boolean isSuccess;
	private String exception;
	private List<String> ignoredMethods = new LinkedList<String>();
	private long t1;
	private double jvm1;
	
	public WorkingListener(BaseConfig config) {
		logger.debug("TESTING WITH CONFIGURATION:\n\nPROJECT:\t{}\nBUILD:\t\t{}\nTESTSUITE:\t{}\nHW PLATFORM:\t{}\nREP_URL:\t{}\n", config.getProject(), config.getBuild(), config.getTestSuite(), config.getPlatform(), config.getRepUrl());
		core = MonitorCore.getInstance().initialize(config);
		perfMonitor = new PerfMonitor();
		core.getCommunicator().setUpConnection(core.getConfig().getRepUrl() + RelURL.TEST_SUITE + core.getConfig().getTestSuite(), Method.POST);
		
		TestSuiteData testSuiteData = new TestSuiteData(core.getConfig().getProject(), 
														core.getConfig().getBuild(), 
														core.getConfig().getPlatform(), 
														core.getSdf().format(new Date()));
		
		logger.debug("SEND TESTSUITE: {}", core.getConfig().getTestSuite());
		String response = core.getCommunicator().sendDataAndReceiveAnswer(core.getGson().toJson(testSuiteData));
		core.setTestSuiteRunId(core.getGson().fromJson(response, Long.class));
	}
	
	private void initMethod() {
		isSuccess = true;
		exception = null;
	}

	private String getMethodName(Description description) {
		int index;
		String method = description.getMethodName();
		if ((index = method.indexOf('[')) != -1) {
			method = method.substring(0, index);
		}
		return description.getClassName() + "." + method;
	}
			
	private String determineMethodNameWithRunId(Description description) {
		StringBuffer methodName = new StringBuffer(getMethodName(description));
		if (methodName.toString().equals(prevMethodName)) {
			methodName.append(" $" + methodRun++);
		} else {
			prevMethodName = methodName.toString();
			methodRun = 2;
		}
		return methodName.toString();
	}
	
	public void testStarted(Description description) throws java.lang.Exception {
		try {
			t1 = core.getConfig().getSigar().getCpu().getTotal();
		} catch (SigarException e) {
			e.printStackTrace();
		}
		jvm1 = (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / BaseConfig.KB_CONST);
		
		perfMonitor.gainAttributes();
		initMethod();
		String methodName = determineMethodNameWithRunId(description);
		core.getCommunicator().setUpConnection(core.getConfig().getRepUrl() + RelURL.TEST_SUITE_RUN + core.getTestSuiteRunId(), Method.POST);
		logger.debug("SEND METHOD: {}", methodName);
		String response = core.getCommunicator().sendDataAndReceiveAnswer(core.getGson().toJson(methodName));
		core.setTestRunId(core.getGson().fromJson(response, Long.class));
		// send the buffer
		perfMonitor.sendAttributes();
	}

	public void testFinished(Description description) throws java.lang.Exception {
		long t2 = 0;
		try {
			t2 = core.getConfig().getSigar().getCpu().getTotal();
		} catch (SigarException e) {
			e.printStackTrace();
		}
		double jvm2 = (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / BaseConfig.KB_CONST);
		
		perfMonitor.gainAttributes();
		core.addMeasuredAttributeToAttrResultData(A.CPU_TIME, U.MILLISEC, (double) (t2 - t1));
		core.addMeasuredAttributeToAttrResultData(A.JVM_MEMORY_DELTA, U.KB, jvm2 - jvm1);
		perfMonitor.sendAttributes();
		core.sendLastTestRunData(null, isSuccess, exception, null);
	}

	public void testFailure(Failure failure) throws java.lang.Exception {
		isSuccess = false;
		exception = failure.getTrace();
	}

	public void testIgnored(Description description) throws java.lang.Exception {
		ignoredMethods.add(determineMethodNameWithRunId(description));
	}
	
	public void testRunFinished(Result result) throws java.lang.Exception {
		LastTestSuiteData data = new LastTestSuiteData(core.getSdf().format(new Date()), ignoredMethods);
		core.getCommunicator().setUpConnection(core.getConfig().getRepUrl() + RelURL.TEST_SUITE_FINISH + core.getTestSuiteRunId(), Method.POST);
		logger.debug("SEND LAST TESTSUITE DATA");
		// answer is always null
		core.getCommunicator().sendDataAndReceiveAnswer(core.getGson().toJson(data));
	}
	
	public void testRunStarted(Description description) {
	}
	
	public void testAssumptionFailure(Failure failure) {
	}
}
