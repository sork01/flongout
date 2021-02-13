package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BrickGridTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		BrickGrid grid = new BrickGrid(new Rect(0.0, 0.0, 1.0, 1.0), 10, 10);
		
		assertEquals(grid.getRect(), new Rect(0.0, 0.0, 1.0, 1.0));
	}

}
