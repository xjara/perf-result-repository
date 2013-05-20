package cz.vutbr.fit.mis.dip.perfclient.listener.testng;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import cz.vutbr.fit.mis.dip.perfclient.config.BaseConfig;


public class BasePerfListener implements ITestListener {
	private ITestListener listener;
	
	public void initialize(BaseConfig config) {		
		if (config.getRepUrl() == null || config.getProject() == null ||
			config.getBuild() == null || config.getTestSuite() == null ||
			config.getPlatform() == null || config.getSigar().getNativeLibrary() == null) {
			
			listener = new DummyListener();
		} else {
			listener = new WorkingListener(config);
		}
	}
	
	public void onFinish(ITestContext context) {
		listener.onFinish(context);
	}

	public void onStart(ITestContext context) {
		listener.onStart(context);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		listener.onTestFailedButWithinSuccessPercentage(result);
	}

	public void onTestFailure(ITestResult result) {
		listener.onTestFailure(result);
	}

	public void onTestSkipped(ITestResult result) {
		listener.onTestSkipped(result);
	}

	public void onTestStart(ITestResult result) {
		listener.onTestStart(result);
	}
	
	public void onTestSuccess(ITestResult result) {
		listener.onTestSuccess(result);
	}	
}