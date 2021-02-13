package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BrickTest {

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testConstructors()
	{
		Brick b1 = new Brick(1.0, 2.0, 3.0, 4.0);
		Brick b2 = new Brick(new Rect(1.0, 2.0, 3.0, 4.0));
		
		assertEquals(b1.getRect(), b2.getRect());
	}
	
	@Test
	public void testHitpointsAndBroken()
	{
		Brick b1 = new Brick(1.0, 2.0, 3.0, 4.0);
		
		assertTrue(b1.getHp() > 0);
		assertFalse(b1.isBroken());
		
		int hp = b1.getHp();
		
		b1.addHp(1);
		
		assertEquals(hp + 1, b1.getHp());
		
		b1.setHp(0);
		
		assertEquals(0, b1.getHp());
		assertTrue(b1.isBroken());
	}
}
