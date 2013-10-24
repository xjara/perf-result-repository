package annotation;

import java.io.IOException;

import org.testng.annotations.Test;

/*
 * Tyto testy testuji anotaci @Test a dale atributy testu: 
 * timeout,expectedExceptions, description a enabled.
 * V testech neni explicitne definovan PerfMonitor,
 * mereni se tedy provadi implicitne vzdy na zacatku 
 * a konci kazde metody.
 */
public class AnnotTest {
	
	@Test(timeOut=2000, description="Test prekroci timeout 2000 ms.")
	public void overTimeOut() throws InterruptedException {
		Thread.sleep(2500);
	}
	
	@Test(timeOut=2000, description="Test neprekroci timeout 2000 ms.")
	public void underTimeOut() throws InterruptedException {
		Thread.sleep(1500);
	}
	
	@Test(enabled=false, description="Ignorovany test.")
	public void ignoredMethod() {
		System.out.println("Text nebude vypsan, protoze test se vubec neprovede.");
	}
	
	@Test(expectedExceptions=ArrayIndexOutOfBoundsException.class, description="Test vypusti ocekavanou vyjimku ArrayIndexOutOfBounds.")
	public void thrownExpectedException() {
		int[] pole = new int[10];
		pole[10] = 10;
	}
	
	@Test(expectedExceptions=IOException.class, description="Test vypusti vyjimku IOException odlisnou od ocekavane vyjimky ArrayIndexOutOfBounds.")
	public void thrownOtherThanExpectedException() {
		int[] pole = new int[10];
		pole[10] = 10;
	}
	
	@Test(expectedExceptions=IOException.class, description="Test ocekava vypusteni vyjimky IOException, ale zadani vyjimka vypustena neni.")
	public void notThrownException() {
		int a = 10;
	} 
}