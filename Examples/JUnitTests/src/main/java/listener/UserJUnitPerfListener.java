package listener;

import cz.vutbr.fit.mis.dip.perfclient.listener.junit.BasePerfListener;

/*
 * User defined JUnit listener using UserConfig instance.
 */
public class UserJUnitPerfListener extends BasePerfListener {
	public UserJUnitPerfListener() {
		initialize(new UserConfig());
	}
}
