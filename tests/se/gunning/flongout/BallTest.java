package se.gunning.flongout;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.Random;

public class BallTest
{
	private double radius;
	
	@Before
	public void setUp()
	{
		radius = (double)new Random().nextInt(100);
	}
	
	@Test
	public void testConstructor()
	{
		Ball b = new Ball(radius);
		
		assertEquals(radius, b.getRadius(), 0.0);
		assertEquals(0.0, b.getPosition().x, 0.0);
		assertEquals(0.0, b.getPosition().y, 0.0);
		assertEquals(0.0, b.getVelocity().x, 0.0);
		assertEquals(0.0, b.getVelocity().y, 0.0);
	}
	
	@Test
	public void testSettingPosition()
	{
		Ball b = new Ball(radius);
		
		b.setPosition(new Vec2(10.0, 20.0));
		
		assertEquals(10.0, b.getPosition().x, 0.0);
		assertEquals(20.0, b.getPosition().y, 0.0);
		
		b.setPosition(new Vec2(-10.0, -20.0));
		
		assertEquals(-10.0, b.getPosition().x, 0.0);
		assertEquals(-20.0, b.getPosition().y, 0.0);
	}
	
	@Test
	public void testSettingVelocity()
	{
		Ball b = new Ball(radius);
		
		b.setVelocity(new Vec2(4, 5));
		
		assertEquals(4.0, b.getVelocity().x, 0.0);
		assertEquals(5.0, b.getVelocity().y, 0.0);
		
		b.setVelocity(new Vec2(-10.0, -20.0));
		
		assertEquals(-10.0, b.getVelocity().x, 0.0);
		assertEquals(-20.0, b.getVelocity().y, 0.0);
	}
	
	@Test
	public void testMoving()
	{
		Ball b = new Ball(radius);
		
		b.setPosition(new Vec2(10.0, 20.0));
		b.move(-15.0, 5.0);
		
		assertEquals(-5.0, b.getPosition().x, 0.0);
		assertEquals(25.0, b.getPosition().y, 0.0);
		
		b.move(new Vec2(2.5, 3.5));
		
		assertEquals(-2.5, b.getPosition().x, 0.0);
		assertEquals(28.5, b.getPosition().y, 0.0);
	}
	
	@Test
	public void testAccelerating()
	{
		Ball b = new Ball(radius);
		
		b.setVelocity(new Vec2(5.0, -7.0));
		b.accelerate(3.0, 2.0);
		
		assertEquals(8.0, b.getVelocity().x, 0.0);
		assertEquals(-5.0, b.getVelocity().y, 0.0);
		
		b.accelerate(new Vec2(0.5, 0.5));
		
		assertEquals(8.5, b.getVelocity().x, 0.0);
		assertEquals(-4.5, b.getVelocity().y, 0.0);
	}
	
	@Test
	public void testPenetrationPower()
	{
		Ball b = new Ball(radius);
		
		assertEquals(1, b.getPenetrationPower());
		
		b.setPenetrationPower(5);
		
		assertEquals(5, b.getPenetrationPower());
	}
}
