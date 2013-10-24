package listener;

//import cz.vutbr.fit.mis.dip.perfclient.config.BaseConfig;
import org.hyperic.sigar.SigarException;

import cz.vutbr.fit.mis.dip.perfclient.attrs.Att;
import cz.vutbr.fit.mis.dip.perfclient.config.Config;
import cz.vutbr.fit.mis.dip.perfclient.constants.A;
import cz.vutbr.fit.mis.dip.perfclient.constants.U;

/*
 * UserConfig class extends standard automatically 
 * measured attributes JVM_MEMORY and SYS_MEMORY in
 * class Config by attribute SYS_ACT_MEMORY.
 * 
 * This class can be also extended the  
 * similar way by BaseConfig class.
 */
public class UserConfig extends Config {

	public UserConfig() {
		// Definition of a new performance attribute SYS_ACT_MEMORY with unit KB.
		
		// system memory except for buffers
		addAutoAttribute(new Att(A.SYS_ACT_MEMORY, U.KB) {
			@Override
			public double gainValue(){
				double value = 0.0;
				try {
					value = sigar.getMem().getActualUsed() / KB_CONST;
				} catch (SigarException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return value;
			}
		});	
	}
}
