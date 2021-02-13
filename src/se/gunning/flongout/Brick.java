package se.gunning.flongout;

import java.util.Random;

/**
 * Represents a brick.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-16
 */
public class Brick
{
    /**
     * Hitpoints remaining.
     */
	private int hp;

    
    /**
     * Rectangle occupied by this brick.
     */
	private Rect rect;
	
    /**
     * Create a new brick.
     *
     * @param x Starting X coordinate
     * @param y Starting Y coordinate
     * @param width Width
     * @param height Height
     */
	public Brick(double x, double y, double width, double height)
	{
		this(new Rect(x, y, width, height));
	}
	
    /**
     * Create a new brick.
     *
     * @param r Rectangle occupied by the brick
     */
	public Brick(Rect r)
	{
		rect = r;
		Random nr = new Random();
		int rand = 1 + nr.nextInt(3);
		hp = rand;
	}
	
    /**
     * Add hitpoints.
     *
     * @param points Number of points to add
     */
	public void addHp(int points)
	{
		hp += points;
	}
	
    /**
     * Set hitpoints.
     *
     * @param points New number of points
     */
	public void setHp(int points)
	{
		hp = points;
	}
	
    /**
     * Get current hitpoints.
     *
     * @return Current hitpoints
     */
	public int getHp()
	{
		return hp;
	}
	
    /**
     * Get broken status.
     *
     * @return True if the brick is broken, false otherwise
     */
	public boolean isBroken()
	{
		return hp <= 0;
	}
	
    /**
     * Get the rectangle occupied by the brick.
     *
     * @return Rectangle occupied by the brick
     */
	public Rect getRect()
	{
		return rect;
	}
}
