package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Vec2Test
{
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testConstructors()
	{
		Vec2 v = new Vec2(1.3, 2.8);
		
		assertEquals(1.3, v.x, 0.0);
		assertEquals(2.8, v.y, 0.0);
		
		v = new Vec2(3, 9);
		
		assertEquals(3.0, v.x, 0.0);
		assertEquals(9.0, v.y, 0.0);
	}
	
	@Test
	public void testToString()
	{
		Vec2 v = new Vec2(1.3, 2.8);
		
		assertEquals("(1.3, 2.8)", v.toString());
	}
	
	@Test
	public void testDotProducts()
	{
		Vec2 v = new Vec2(3, 5);
		Vec2 u = new Vec2(2, -2);
		
		assertEquals(34.0, v.dot(v), 0.0);
		assertEquals(-4.0, v.dot(u), 0.0);
		assertEquals(-4.0, u.dot(v), 0.0);
		assertEquals(8.0, u.dot(u), 0.0);
		
		assertEquals(v.dot(v), Vec2.dot(v, v), 0.0);
		assertEquals(v.dot(u), Vec2.dot(v, u), 0.0);
		assertEquals(v.dot(u), Vec2.dot(u, v), 0.0);
		assertEquals(u.dot(u), Vec2.dot(u, u), 0.0);
	}
	
	@Test
	public void testLength()
	{
		Vec2 v = new Vec2(1.0, 1.0);
		assertEquals(Math.sqrt(2), v.length(), 0.0);
		
		Vec2 u = new Vec2(3.0, 4.0);
		assertEquals(5.0, u.length(), 0.0);
	}
	
	@Test
	public void testAddAndSubtract()
	{
		Vec2 v = new Vec2(1.0, 1.0);
		
		assertEquals(2.0, v.add(new Vec2(1.0, 0.0)).x, 0.0);
		assertEquals(1.0, v.add(new Vec2(1.0, 0.0)).y, 0.0);
		
		assertEquals(-9.0, v.add(new Vec2(-10.0, 0.0)).x, 0.0);
		assertEquals(11.0, v.add(new Vec2(0.0, 10.0)).y, 0.0);
		
		assertEquals(2.0, v.add(v).x, 0.0);
		assertEquals(2.0, v.add(v).y, 0.0);
		
		assertEquals(0.0, v.subtract(v).x, 0.0);
		assertEquals(0.0, v.subtract(v).y, 0.0);
	}
	
	@Test
	public void testScale()
	{
		Vec2 v = new Vec2(1.0, 1.0);
		
		assertEquals(2.0, v.scale(2.0).x, 0.0);
		assertEquals(5.0, v.scale(5.0).y, 0.0);
	}
	
	@Test
	public void testNormalize()
	{
		Vec2 v = new Vec2(1.52, 5.92);
		
		assertEquals(1.0, v.normalize().length(), 1e-14);
	}
	
	@Test
	public void testGetAngle()
	{
		Vec2 v1 = new Vec2(1.0, 0.0);
		Vec2 v2 = new Vec2(1.0, 1.0);
		Vec2 v3 = new Vec2(0.0, 1.0);
		Vec2 v4 = new Vec2(-1.0, 1.0);
		Vec2 v5 = new Vec2(-1.0, 0.0);
		Vec2 v6 = new Vec2(-1.0, -1.0);
		Vec2 v7 = new Vec2(0.0, -1.0);
		Vec2 v8 = new Vec2(1.0, -1.0);
		
		assertEquals(0.0, v1.getAngle(), 1e-14);
		assertEquals(Math.PI / 4.0, v2.getAngle(), 1e-14);
		assertEquals(Math.PI / 2.0, v3.getAngle(), 1e-14);
		assertEquals(3.0 * Math.PI / 4.0, v4.getAngle(), 1e-14);
		assertEquals(Math.PI, v5.getAngle(), 1e-14);
		assertEquals(-3.0 * Math.PI / 4.0, v6.getAngle(), 1e-14);
		assertEquals(-Math.PI / 2.0, v7.getAngle(), 1e-14);
		assertEquals(-Math.PI / 4.0, v8.getAngle(), 1e-14);
	}
	
	@Test
	public void testSetAngle()
	{
		Vec2 v = new Vec2(1.0, 1.0);
		
		assertEquals(Math.PI / 7.0, v.setAngle(Math.PI / 7.0).getAngle(), 1e-14);
		assertEquals(-Math.PI / 7.0, v.setAngle(-Math.PI / 7.0).getAngle(), 1e-14);
		assertEquals(0.0, v.setAngle(8.0 * Math.PI).getAngle(), 1e-14);
		assertEquals(-Math.PI / 2.0, v.setAngle(3.0 * Math.PI / 2.0).getAngle(), 1e-14);
	}
	
	@Test
	public void testRotate()
	{
		Vec2 v = new Vec2(1.0, 1.0);
		
		assertEquals(0.0, v.rotate(Math.PI / 4.0).x, 1e-14);
		assertEquals(-1.0, v.rotate(Math.PI).x, 1e-14);
	}
	
	@Test
	public void testProjection()
	{
		Vec2 v = new Vec2(5.0, 0.0);
		
		assertEquals(0.0, new Vec2(0.0, 10.0).projectOnto(v).length(), 1e-14);
		assertEquals(5.0, new Vec2(5.0, 10.0).projectOnto(v).length(), 1e-14);
	}
	
	@Test
	public void testCompare()
	{
		Vec2 v = new Vec2(1.0, 1.0);
		Vec2 u = new Vec2(2.0, 2.0);
		
		assertEquals(-1, v.compareTo(u));
		assertEquals(1, u.compareTo(v));
	}
}
