package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.gunning.flongout.Powerup.DropDirection;

import java.util.HashSet;

public class PowerupFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetRandomPowerup() {
		
		PowerupFactory pf = new PowerupFactory();
		
		HashSet<String> seen = new HashSet<String>();
		
		for (int i = 0; i < 100000; ++i)
		{
			seen.add(pf.createRandomPowerup(new Vec2(0, 0), DropDirection.LEFT).getClass().toString());
		}
		
		assertEquals(8, seen.size());
	}

}
