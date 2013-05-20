package cz.vutbr.fit.mis.dip.perfclient.config;

import java.util.LinkedList;
import java.util.List;

import org.hyperic.sigar.Sigar;

import cz.vutbr.fit.mis.dip.perfclient.attrs.Att;



public class BaseConfig {
	private static final String APPLICATION = "PerfMonitor";
	protected final String repUrl;
	protected final String project;
	protected final String build;
	protected final String testSuite;
	protected final String platform;
	protected final Sigar sigar;
	
	protected List<Att> autoAttributes;
	
	// constants
	public static final int KB_CONST = 1024;
	public static final int MB_CONST = 1024 * 1024;
	
	public BaseConfig() {
		repUrl = System.getProperty(APPLICATION + ".repURL");
		project = System.getProperty(APPLICATION + ".project");
		build = System.getProperty(APPLICATION + ".build");
		testSuite = System.getProperty(APPLICATION + ".testsuite");
		platform = System.getProperty(APPLICATION + ".platform");
		sigar = new Sigar();
		autoAttributes = new LinkedList<Att>();
	}
	
	public String getRepUrl() {
		return repUrl;
	}
	
	public String getProject() {
		return project;
	}
	
	public String getBuild() {
		return build;
	}
	
	public String getTestSuite() {
		return testSuite;
	}

	public String getPlatform() {
		return platform;
	}

	public Sigar getSigar() {
		return sigar;
	}
	
	public List<Att> getAutoAttributes() {
		return autoAttributes;
	}
	
	public void setAutoAttributes(List<Att> autoAttributes) {
		this.autoAttributes = autoAttributes;
	}
	
	public void addAutoAttribute(Att attribute) {
		autoAttributes.add(attribute);
	}
}
