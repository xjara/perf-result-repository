package cz.vutbr.fit.mis.dip.perfclient.listener.testng;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import cz.vutbr.fit.mis.dip.perfclient.config.BaseConfig;
import cz.vutbr.fit.mis.dip.perfclient.constants.A;
import cz.vutbr.fit.mis.dip.perfclient.communicator.Method;
import cz.vutbr.fit.mis.dip.perfclient.constants.U;
import cz.vutbr.fit.mis.dip.perfclient.monitor.MonitorCore;
import cz.vutbr.fit.mis.dip.perfclient.monitor.PerfMonitor;
import cz.vutbr.fit.mis.dip.perfobjects.constants.RelURL;
import cz.vutbr.fit.mis.dip.perfobjects.objects.LastTestSuiteData;
import cz.vutbr.fit.mis.dip.perfobjects.objects.TestSuiteData;


public class WorkingListener implements ITestListener {
	private static final Logger logger = LoggerFactory.getLogger(WorkingListener.class);
	private PerfMonitor perfMonitor;
	private MonitorCore core;
	private String prevMethodName = null;
	private int methodRun;
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
	
	private String getMethodName(ITestResult result) {
		return result.getInstanceName() + "." + result.getMethod().getMethodName();
	}
	
	private String determineMethodNameWithRunId(ITestResult result) {
		StringBuffer methodName = new StringBuffer(getMethodName(result));
		if (methodName.toString().equals(prevMethodName)) {
			methodName.append(" $" + methodRun++);
		} else {
			prevMethodName = methodName.toString();
			methodRun = 2;
		}
		return methodName.toString();
	}
	
	public void onTestStart(ITestResult result) {
		try {
			t1 = core.getConfig().getSigar().getCpu().getTotal();
		} catch (SigarException e) {
			e.printStackTrace();
		}
		jvm1 = (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / BaseConfig.KB_CONST);
		
		perfMonitor.gainAttributes();
		String methodName = determineMethodNameWithRunId(result);
		core.getCommunicator().setUpConnection(core.getConfig().getRepUrl() + RelURL.TEST_SUITE_RUN + core.getTestSuiteRunId(), Method.POST);
		logger.debug("SEND METHOD: {}", methodName);
		String response = core.getCommunicator().sendDataAndReceiveAnswer(core.getGson().toJson(methodName));
		core.setTestRunId(core.getGson().fromJson(response, Long.class));
		// send the buffer
		perfMonitor.sendAttributes();
	}

	private String determineException(ITestResult result) {
		StringBuilder msg = null;
		if (result.isSuccess() == false) {
			// get the exception of the method
			msg = new StringBuilder().append(result.getThrowable().getMessage() + "\n");
			StackTraceElement[] lines = result.getThrowable().getStackTrace();
			for(int i = 0; i < lines.length; i++) {
				msg.append("\t" + lines[i] + "\n");
			}
		}
		return msg == null? null : msg.toString();
	}
	
	private String determineParameters(ITestResult result) {
		Object[] pars = result.getParameters();
		StringBuffer sb = null;
		if (pars.length > 0) {
			sb = new StringBuffer().append(pars[0].toString());
			for(int i = 1; i < pars.length; i++) {
				sb.append(", " + pars[i].toString());
			}
		}
		return sb == null? null : sb.toString();
	}
	
	private void processFinishedMethod(ITestResult result) {
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
		core.sendLastTestRunData(result.getMethod().getDescription(), result.isSuccess(), determineException(result), determineParameters(result));
	}
	
	public void onTestSuccess(ITestResult result) {
		processFinishedMethod(result);
	}

	public void onTestFailure(ITestResult result) {
		processFinishedMethod(result);
	}
	
	private List<String> determineExcludedMethods(ITestContext context) {
		List<String> excludedMethods = new LinkedList<String>();
		for(ITestNGMethod testNGMethod: context.getExcludedMethods()) {
			String testClass = testNGMethod.getTestClass().toString();
			testClass = testClass.split(" ")[2];
			testClass = testClass.substring(0, testClass.length() - 1);
			excludedMethods.add(testClass + "." + testNGMethod.getMethodName());
		}
		return excludedMethods;
	}
	
	public void onFinish(ITestContext context) {	
		LastTestSuiteData data = new LastTestSuiteData(core.getSdf().format(context.getEndDate()), determineExcludedMethods(context));
		core.getCommunicator().setUpConnection(core.getConfig().getRepUrl() + RelURL.TEST_SUITE_FINISH + core.getTestSuiteRunId(), Method.POST);
		logger.debug("SEND LAST TESTSUITE DATA");
		// answer is always null
		core.getCommunicator().sendDataAndReceiveAnswer(core.getGson().toJson(data));
	}
	
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
	}
	
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub	
	}
}
