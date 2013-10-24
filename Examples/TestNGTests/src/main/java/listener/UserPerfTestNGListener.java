package listener;

import cz.vutbr.fit.mis.dip.perfclient.listener.testng.BasePerfListener;

/*
 * User defined TestNG listener using UserConfig instance.
 */
public class UserPerfTestNGListener extends BasePerfListener {
	public UserPerfTestNGListener() {
		initialize(new UserConfig());
	}
}
