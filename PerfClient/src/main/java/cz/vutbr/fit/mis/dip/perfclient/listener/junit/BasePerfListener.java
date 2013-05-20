package cz.vutbr.fit.mis.dip.perfclient.listener.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import cz.vutbr.fit.mis.dip.perfclient.config.BaseConfig;


public class BasePerfListener extends RunListener  {
	private RunListener listener;
	
	public void initialize(BaseConfig config) {		
		if (config.getRepUrl() == null || config.getProject() == null ||
			config.getBuild() == null || config.getTestSuite() == null ||
			config.getPlatform() == null || config.getSigar().getNativeLibrary() == null) {
			
			listener = new RunListener();
		} else {
			listener = new WorkingListener(config);
		}
	}
	
	public void testRunStarted(Description description)	throws java.lang.Exception {
		listener.testRunStarted(description);
	}

	public void testRunFinished(Result result) throws java.lang.Exception {
		listener.testRunFinished(result);
	}

	public void testStarted(Description description) throws java.lang.Exception {
		listener.testStarted(description);
	}

	public void testFinished(Description description) throws java.lang.Exception {
		listener.testFinished(description);
	}

	public void testFailure(Failure failure) throws java.lang.Exception {
		listener.testFailure(failure);
	}

	public void testIgnored(Description description) throws java.lang.Exception {
		listener.testIgnored(description);
	}
	
	public void testAssumptionFailure(Failure failure) {
		listener.testAssumptionFailure(failure);
	}
}
