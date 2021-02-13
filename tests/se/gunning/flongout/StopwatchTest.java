package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StopwatchTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		Stopwatch sw = new Stopwatch();
		
		assertEquals(0, sw.getSum());
		assertEquals(0, sw.getSeconds());
	}
	
	@Test
	public void testMultipleStartStop()
	{
		Stopwatch sw = new Stopwatch();
		
		sw.start();
		sw.start();
		sw.start();
		
		sw.stop();
		sw.stop();
		sw.stop();
	}
	
	@Test
	public void testResetAtZero()
	{
		Stopwatch sw = new Stopwatch();
		
		sw.reset();
		
		assertEquals(0, sw.getSum());
		assertEquals(0, sw.getSeconds());
	}
	
	@Test
	public void testTimerAndReset()
	{
		Stopwatch sw = new Stopwatch();
		
		sw.start();
		
		try { Thread.sleep(500); } catch (InterruptedException e) {}
		
		sw.stop();
		
		assertTrue(sw.getSum() >= 500);
		
		try { Thread.sleep(500); } catch (InterruptedException e) {}
		
		assertTrue(sw.getSum() < 1000);
		
		sw.reset();
		
		assertEquals(0, sw.getSum());
		assertEquals(0, sw.getSeconds());
	}
}
