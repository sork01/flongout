package se.gunning.flongout;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Base for powerups/powerdowns.
 *  
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public abstract class Powerup
{
	/**
	 * Powerup drop (flight) direction.
	 *  
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
	 */
	public enum DropDirection
	{
		LEFT, RIGHT
	};
	
	/**
	 * Powerup types.
	 *  
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-19
	 */
	public enum Type
	{
		GOOD, BAD, NEUTRAL
	};
	
	/**
	 * Position of the powerup.
	 */
	private Vec2 position;
	
	/**
	 * Velocity of the powerup.
	 */
	private Vec2 velocity;
	
	/**
	 * Drop direction for the powerup.
	 */
	private DropDirection dropDirection;
	
	/**
	 * Paddle that picked up this powerup.
	 */
	@SuppressWarnings("unused")
	private Paddle owner;
	
	/**
	 * Color of this powerup.
	 */
	protected Color color;
	
	/**
	 * Create a new powerup.
	 * 
	 * @param pos Initial position
	 * @param dir Drop direction
	 */
	public Powerup(Vec2 pos, DropDirection dir)
	{
		position = pos;
		dropDirection = dir;
		color = new Color(0.0f, 1.0f, 0.0f);
		
		// set an appropriate velocity
		if (dropDirection == DropDirection.LEFT)
		{
			velocity = new Vec2(-0.05, 0.0);
		}
		else
		{
			velocity = new Vec2(0.05, 0.0);
		}
	}
	
	/**
	 * Move the powerup.
	 */
	public void move()
	{
		position = position.add(new Vec2(velocity.x, 0.04*Math.sin(position.x*2)));
	}
	
	/**
	 * Get the position of the powerup.
	 * 
	 * @return Position of the powerup
	 */
	public Vec2 getPosition()
	{
		return position;
	}
	
	/**
	 * Get the radius that defines the pickup circle for
	 * this powerup.
	 * 
	 * @return Radius defining the pickup circle
	 */
	public double getPickupRadius()
	{
		return 0.25;
	}
	
	/**
	 * Have a paddle pick up this powerup.
	 * 
	 * @param collector Paddle that picked up the powerup
	 */
	public void becomePickedUpBy(Paddle collector)
	{
		owner = collector;
	}
	
	/**
	 * Apply the effects of the powerup.
	 * 
	 * @param collector Paddle that picked up the powerup
	 * @param other The other paddle
	 * @param game The game scene 
	 */
	public abstract void applyStaticEffects(Paddle collector, Paddle other, GameScene game);
	
	/**
	 * Remove the effects of the powerup.
	 * 
	 * @param collector Paddle that picked up the powerup
	 * @param other The other paddle
	 * @param game The game scene 
	 */
	public abstract void removeStaticEffects(Paddle collector, Paddle other, GameScene game);
	
	/**
	 * Get the powerup type of this powerup.
	 * 
	 * @return The powerup type
	 */
	abstract public Type getType();
	
	/**
	 * Get a description of this powerup.
	 * 
	 * @return Description of the powerup
	 */
	abstract public String getText();
	
	/**
	 * Draw the pickup circle for the powerup. Not intended for production.
	 * 
	 * @param g Slick2D Graphics
	 * @param ct Coordinate transformer
	 */
	public void debugDraw(Graphics g, CoordinateTransformer ct)
	{
		g.setLineWidth(2.0f);
		g.setColor(color);
		g.drawOval(ct.toScreenX(position.x - 0.125), ct.toScreenY(position.y + 0.125), ct.widthToScreen(0.25), ct.heightToScreen(0.25));
	}
}
