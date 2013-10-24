package listener;

import cz.vutbr.fit.mis.dip.perfclient.attrs.Att;
import cz.vutbr.fit.mis.dip.perfclient.config.BaseConfig;
import cz.vutbr.fit.mis.dip.perfclient.constants.A;
import cz.vutbr.fit.mis.dip.perfclient.constants.U;

/*
 * UserConfig class extends empty set of automatically 
 * measured attributes in class BaseConfig and just
 * one attribute JVM_MEMORY is added.
 * 
 * This class can be also extended the  
 * similar way by Config class.
 */
public class UserConfig extends BaseConfig {
	
	public UserConfig() {
		// Definition of just one performance attribute JVM_MEMORY with unit KB.
		
		addAutoAttribute(new Att(A.JVM_MEMORY, U.KB) {
			@Override
			public double gainValue() {
				return (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / KB_CONST);
			}
		});
	}
}
