package se.gunning.flongout;

/**
 * Represents a ball.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-11
 */
public class Ball
{
	/**
	 * Position of the ball.
	 */
	private Vec2 position;
	
	/**
	 * Velocity of the ball.
	 */
	private Vec2 velocity;
	
	/**
	 * Radius of the ball.
	 */
	private double radius;
	
	/**
	 * Penetration power of the ball.
	 */
	private int penetrationPower = 1;
	
	/**
	 * Create a new ball with a given radius. The initial position
	 * of the ball will be at (0, 0), and the initial velocity will 
	 * be (0, 0).
	 * 
	 * @param radius Radius to set
	 */
	public Ball(double radius)
	{
		this.radius = radius;
		position = new Vec2(0, 0);
		velocity = new Vec2(0, 0);
	}
	
	/**
	 * Get the position of the ball.
	 * 
	 * @return Position of the ball
	 */
	public Vec2 getPosition()
	{
		return position;
	}
    
	/**
	 * Set the position of the ball.
	 * 
	 * @param v New position to set
	 */
	public void setPosition(Vec2 v)
	{
		position = v;
	}
	
	/**
	 * Get the velocity of the ball.
	 * 
	 * @return The velocity of the ball
	 */
	public Vec2 getVelocity()
	{
		return velocity;
	}
	
	/**
	 * Set the velocity of the ball.
	 * 
	 * @param v New velocity to set
	 */
	public void setVelocity(Vec2 v)
	{
		velocity = v;
	}
	
	/**
	 * Accelerate the ball.
	 * 
	 * @param x Amount to accelerate horizontally
	 * @param y Amount to accelerate vertically
	 */
	public void accelerate(double x, double y)
	{
		accelerate(new Vec2(x, y));
	}
	
	/**
	 * Accelerate the ball.
	 * 
	 * @param acc Acceleration vector
	 */
	public void accelerate(Vec2 acc)
	{
		velocity = velocity.add(acc);
	}
	
	/**
	 * Move the ball.
	 * 
	 * @param x Distance to move horizontally
	 * @param y Distance to move vertically
	 */
	public void move(double x, double y)
	{
		move(new Vec2(x, y));
	}
	
	/**
	 * Move the ball.
	 * 
	 * @param v Movement vector
	 */
	public void move(Vec2 v)
	{
		position = position.add(v);
	}
	
	/**
	 * Get the radius of the ball.
	 * 
	 * @return Radius of the ball
	 */
	public double getRadius()
	{
		return radius;
	}
	
    /**
     * Set the radius of the ball.
     *
     * @param radius New radius of the ball
     */
	public void setRadius(double radius)
	{
		this.radius = radius;
	}
	
    /**
     * Get the penetration power of the ball.
     *
     * @return The penetration power
     */
	public int getPenetrationPower()
	{
		return penetrationPower;
	}
	
    /**
     * Set the penetration power of the ball.
     *
     * @param n New penetration power
     */
	public void setPenetrationPower(int n)
	{
		penetrationPower = n;
	}
}
