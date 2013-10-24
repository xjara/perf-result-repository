package param;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cz.vutbr.fit.mis.dip.perfclient.monitor.PerfMonitor;

/*
 * Testovani podpory parametrizovanych testu.
 */
public class ParamTest {
	private PerfMonitor p = new PerfMonitor();
	
	@DataProvider(name="pv")
	public Object[][] createData() {
		return new Object[][] {
			{ "00000000", 7},
			{ "1111111111", 10},
			{ "221222", 5}};
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
	
	@Test(dataProvider="pv", description="Parametrizovany test, ktery testuje, zda je 1.parametr palindrom a zaroven je delsi nez hodnota 2.parametru.")
	public void isPalindromOfEnoughLength(String s, int length) {
		Assert.assertEquals(true, s.length() > length? isPalindrom(s) : false);
	} 
}
