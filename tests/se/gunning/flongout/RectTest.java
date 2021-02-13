package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RectTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		Rect r = new Rect(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(1.0, r.getX(), 1e-14);
		assertEquals(2.0, r.getY(), 1e-14);
		assertEquals(3.0, r.getWidth(), 1e-14);
		assertEquals(4.0, r.getHeight(), 1e-14);
	}
	
	@Test
	public void testCopyConstructor() {
		Rect base = new Rect(1.0, 2.0, 3.0, 4.0);
		Rect r = new Rect(base);
		
		assertEquals(1.0, r.getX(), 1e-14);
		assertEquals(2.0, r.getY(), 1e-14);
		assertEquals(3.0, r.getWidth(), 1e-14);
		assertEquals(4.0, r.getHeight(), 1e-14);
	}
	
	@Test
	public void testGetCenter()
	{
		Rect r = new Rect(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(2.5, r.getCenter().x, 1e-14);
		assertEquals(4.0, r.getCenter().y, 1e-14);
	}
	
	@Test
	public void testContainsPoint()
	{
		Rect r = new Rect(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(true, r.containsPoint(new Vec2(2.5, 4.0)));
		assertEquals(false, r.containsPoint(new Vec2(12.5, 14.0)));
	}
	
	@Test
	public void testDistanceToPoint()
	{
		Rect r = new Rect(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(0.0, r.distanceToPoint(new Vec2(1.0, 2.0)), 1e-14);
		assertEquals(0.0, r.distanceToPoint(new Vec2(4.0, 2.0)), 1e-14);
		assertEquals(0.0, r.distanceToPoint(new Vec2(4.0, 6.0)), 1e-14);
		assertEquals(0.0, r.distanceToPoint(new Vec2(1.0, 6.0)), 1e-14);
		
		assertEquals(Math.sqrt(2), r.distanceToPoint(new Vec2(0.0, 1.0)), 1e-14);
	}
	
	@Test
	public void testShortestVectorToPoint()
	{
		Rect r = new Rect(1.0, 2.0, 3.0, 4.0);
		
		assertEquals(-1.0, r.shortestVectorToPoint(new Vec2(0.0, 0.0)).x, 1e-14);
		assertEquals(-2.0, r.shortestVectorToPoint(new Vec2(0.0, 0.0)).y, 1e-14);
		
		assertEquals(1.0, r.shortestVectorToPoint(new Vec2(5.0, 3.0)).x, 1e-14);
		assertEquals(0.0, r.shortestVectorToPoint(new Vec2(5.0, 3.0)).y, 1e-14);
	}
	
	@Test
	public void testToString()
	{
		Rect r = new Rect(1.0, 2.0, 3.0, 4.0);
		
		assertEquals("Rect{x:1.0, y:2.0, width:3.0, height:4.0}", r.toString());
	}
	
	@Test
	public void testEquals()
	{
		Rect r1 = new Rect(1.0, 2.0, 3.0, 4.0);
		Rect r2 = new Rect(1.0, 2.0, 3.0, 4.0);
		Rect r3 = new Rect(4.0, 3.0, 2.0, 1.0);
		
		assertEquals(r1, r2);
		assertFalse(r1.equals(r3));
	}
}
