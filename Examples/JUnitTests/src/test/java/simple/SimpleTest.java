package simple;

import org.junit.Assert;
import org.junit.Test;

import cz.vutbr.fit.mis.dip.perfclient.monitor.PerfMonitor;

/*
 * V techto testech je jiz definovan PerfMonitor.
 * Ten provadi v kazdem testu vice mereni a v jeho 
 * prubehu odesila i vykonnostni data.
 */
public class SimpleTest {
	
	private static final int ITEMS = 10000;
	private PerfMonitor p = new PerfMonitor();
	
	// Test vytvori 2 pole o 10000 prvcich. V prvnim poli jsou hodnoty 
	// od 1 do 10000 a ve druhem od 10000 do 19999. Nakonec porovna hodnotu 
	// souctu sum prvku v obou polich s ocekavanou hodnotou 200000000.
	@Test
	public void arithmeticRow() throws InterruptedException {
		// inicializace pole1
		int[] pole1 = new int[ITEMS];
		p.gainAttributes();
		// naplneni pole1
		for(int i = 0; i < ITEMS; i++) {
			pole1[i] = i + 1;
		}
		p.gainAttributesAndSend();
		
		// cekani
		Thread.sleep(10000);
		// inicializace pole2
		int[] pole2 = new int[ITEMS];
		p.gainAttributesAndSend();
		// naplneni pole2
		for(int i = 0; i < ITEMS; i++) {
			pole2[i] = ITEMS + i;
		}
		p.gainAttributes();
		
		Thread.sleep(10000);
		// secteni hodnot v obou polich
		int suma = 0;
		for(int i = 0; i < ITEMS; i++) {
			suma += pole1[i] + pole2[i];
		}
		p.gainAttributes();
		Assert.assertEquals(200000000, suma);
	}
	
	private int fib(int n) {
		p.gainAttributesAndSend();
		if (n < 2) {
			return n;
		} else {
			return fib(n - 1) + fib(n - 2);
		}
	}
	
	// Metoda rekurzivne urci 10-ty clen Fibonacciho rady a 
	// porovna jej s hodnotou 10. Pri kazdem rekurzivnim volani se 
	// ziskaji automaticky merene vykonnostni atributy a odeslou se.
	@Test
	public void get10MemberOfFibonacciRowAndCompareWith10() {
		Assert.assertEquals(10, fib(10));
	}
}
