package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CounterTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		Counter c = new Counter();
		
		assertEquals(0, c.getCount());
		assertTrue(c.isZero());
		assertEquals("0", c.toString());
	}
	
	@Test
	public void testAdding()
	{
		Counter c = new Counter();
		
		assertEquals(0, c.getCount());
		c.incrementByOne();
		assertEquals(1, c.getCount());
		c.setCount(13);
		assertEquals(13, c.getCount());
		assertFalse(c.isZero());
		c.addCount(2);
		assertEquals(15, c.getCount());
		assertEquals("15", c.toString());
	}
	
	@Test
	public void testDecrease()
	{
        Counter c = new Counter();
		
		c.setCount(2);
		assertEquals(2, c.getCount());
		assertFalse(c.isZero());
		c.decrementByOne();
		assertEquals(1, c.getCount());
		c.decrementByOne();
		assertEquals(0, c.getCount());
		assertTrue(c.isZero());
		assertEquals("0", c.toString());
	}
}