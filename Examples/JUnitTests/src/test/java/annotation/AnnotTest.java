package annotation;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

/*
 * Tyto testy testuji anotaci @Test a @Ignore  
 * a dale atributy testu: timeout a exception.
 * V testech neni explicitne definovan PerfMonitor,
 * mereni se tedy provadi implicitne vzdy na zacatku 
 * a konci kazde metody.
 */
public class AnnotTest {

	// Test prekroci timeout 2000 ms.
	@Test(timeout=2000)
	public void overTimeOut() throws InterruptedException {
		Thread.sleep(2500);
	}
	
	// Test neprekroci timeout 2000 ms.
	@Test(timeout=2000)
	public void underTimeOut() throws InterruptedException {
		Thread.sleep(1500);
	}
	
	// Ignorovany test.
	@Test
	@Ignore
	public void ignoredMethod() {
		System.out.println("Text nebude vypsan, protoze test se vubec neprovede.");
	}
	
	// Test vypusti ocekavanou vyjimku ArrayIndexOutOfBounds.
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void thrownExpectedException() {
		int[] pole = new int[10];
		pole[10] = 10;
	}
	
	// Test vypusti vyjimku IOException odlisnou od ocekavane vyjimky ArrayIndexOutOfBounds.
	@Test(expected=IOException.class)
	public void thrownOtherThanExpectedException() {
		int[] pole = new int[10];
		pole[10] = 10;
	}
	
	// Test ocekava vypusteni vyjimky IOException, ale zadani vyjimka vypustena neni.
	@Test(expected=IOException.class)
	public void notThrownException() {
		int a = 10;
	}
}
