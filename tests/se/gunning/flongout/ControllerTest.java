package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		Controller c = new Controller();
		
		assertEquals(0.0, c.leftAnalog.getMagnitude(), 1e-14);
		assertEquals(0.0, c.rightAnalog.getMagnitude(), 1e-14);
		
		assertFalse(c.buttonOne.isPressed());
		assertFalse(c.buttonTwo.isPressed());
		assertFalse(c.buttonThree.isPressed());
	}
	
	@Test
	public void testButtons()
	{
		Controller c = new Controller();
		
		c.buttonOne.press();
		
		assertTrue(c.buttonOne.isPressed());
		assertFalse(c.buttonTwo.isPressed());
		assertFalse(c.buttonThree.isPressed());
		
		c.buttonTwo.press();
		
		assertTrue(c.buttonOne.isPressed());
		assertTrue(c.buttonTwo.isPressed());
		assertFalse(c.buttonThree.isPressed());
		
		c.buttonOne.release();
		c.buttonThree.press();
		
		assertFalse(c.buttonOne.isPressed());
		assertTrue(c.buttonTwo.isPressed());
		assertTrue(c.buttonThree.isPressed());
		
		c.buttonTwo.release();
		c.buttonThree.release();
		
		assertFalse(c.buttonOne.isPressed());
		assertFalse(c.buttonTwo.isPressed());
		assertFalse(c.buttonThree.isPressed());
	}
	
	// TODO: test analog sticks
}
