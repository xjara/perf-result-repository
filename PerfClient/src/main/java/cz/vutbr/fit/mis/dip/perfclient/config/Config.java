package cz.vutbr.fit.mis.dip.perfclient.config;

import org.hyperic.sigar.SigarException;

import cz.vutbr.fit.mis.dip.perfclient.constants.A;
import cz.vutbr.fit.mis.dip.perfclient.constants.U;
import cz.vutbr.fit.mis.dip.perfclient.attrs.Att;

public class Config extends BaseConfig {
	
	public Config() {
		// attributes CPU_TIME and JVM_MEMORY_DELTA are implicitly measured
		
		addAutoAttribute(new Att(A.JVM_MEMORY, U.KB) {
			@Override
			public double gainValue() {
				return (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / KB_CONST);
			}
		});
		
		// including buffers
		addAutoAttribute(new Att(A.SYS_MEMORY, U.KB) {
			@Override
			public double gainValue() {
				double value = 0.0;
				try {
					value = sigar.getMem().getUsed() / KB_CONST;
				} catch (SigarException e) {
					e.printStackTrace();
				}
				return value;
			}
		});
	}
}