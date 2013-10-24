package param;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import cz.vutbr.fit.mis.dip.perfclient.monitor.PerfMonitor;

/*
 * Testovani podpory parametrizovanych testu.
 */
@RunWith(Parameterized.class)
public class ParamTest {
	
	private String s;
	private int length;
	private PerfMonitor p = new PerfMonitor();
	
	public ParamTest(String s, int length) {
		this.s = s;
		this.length = length;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { 
				{ "00000000", 7 },
				{ "1111111111", 10 }, 
				{ "221222", 5 }};
		
		return Arrays.asList(data);
	}

	private boolean isPalindrom(String s) {
		for(int i = 0, j = s.length() - 1; i < j; i++, j--) {
			p.gainAttributesAndSend();
			if (s.charAt(i) != s.charAt(j)) {
				return false;
			}
		}
		return true;
	}
	
	// Parametrizovany test, ktery testuje, zda je retezec v atributu s palindrom a zaroven je delsi nez hodnota atributu length.
	@Test
	public void isPalindromOfEnoughLength() {
		Assert.assertEquals(true, s.length() > length? isPalindrom(s) : false);
	}
}
