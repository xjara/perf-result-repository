package cz.vutbr.fit.mis.dip.perfclient.listener.junit;

import cz.vutbr.fit.mis.dip.perfclient.config.Config;

public class PerfListener extends BasePerfListener {
	public PerfListener() {
		initialize(new Config());
	}
}
