package se.gunning.flongout;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Rect represents a rectangle constructed from a corner (x, y) growing to (width) in
 * positive x and (height) in positive y. In the internal coordinate system of Flongout
 * the rectangle will be constructed from the lower left corner (x, y) and grow (width)
 * to the right / positive x and (height) up / positive y.
 *  
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class Rect
{
	/**
	 * X coordinate of starting corner
	 */
	private double x;
	
	/**
	 * Y coordinate of starting corner
	 */
	private double y;
	
	/**
	 * Width
	 */
	private double width;
	
	/**
	 * Height
	 */
	private double height;
	
	/**
	 * Create a new Rect.
	 * 
	 * @param x X coordinate of starting corner
	 * @param y Y coordinate of starting corner
	 * @param width Width
	 * @param height Height
	 */
	public Rect(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Create a new Rect by copying an existing one.
	 * 
	 * @param other Rectangle to copy
	 */
	public Rect(Rect other)
	{
		x = other.x;
		y = other.y;
		width = other.width;
		height = other.height;
	}
	
	/**
	 * Get the X coordinate of the starting corner.
	 * 
	 * @return X coordinate of starting corner
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Get the Y coordinate of the starting corner.
	 * 
	 * @return Y coordinate of starting corner
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Get the width of the rectangle.
	 * 
	 * @return Width of the rectangle
	 */
	public double getWidth()
	{
		return width;
	}
	
	/**
	 * Get the height of the rectangle.
	 * 
	 * @return Height of the rectangle
	 */
	public double getHeight()
	{
		return height;
	}
	
	/**
	 * Get the coordinates of the center point of the rectangle.
	 * 
	 * @return Coordinates of rectangle center
	 */
	public Vec2 getCenter()
	{
		return new Vec2(x + width / 2.0, y + height / 2.0);
	}
	
	/**
	 * Compute whether or not a given point is inside the rectangle.
	 * 
	 * @param pt A vector (x, y) representing the point to test
	 * @return true if the point lies inside the rectangle, false otherwise.
	 */
	public boolean containsPoint(Vec2 pt)
	{
		if (pt.x < x || pt.y < y || pt.x > x + width || pt.y > y + height)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Compute the shortest distance from the rectangle to a given point. All
	 * sides of the rectangle are considered. Also supports the point being
	 * on the inside of the rectangle.
	 * 
	 * @param pt A vector (x, y) representing the point to test
	 * @return The shortest distance to the point
	 */
	public double distanceToPoint(Vec2 pt)
	{
		return shortestVectorToPoint(pt).length();
	}
	
	/**
	 * Compute the shortest vector from the rectangle to a given point. All
	 * sides of the rectangle are considered. Also supports the point being
	 * on the inside of the rectangle.
	 * 
	 * @param pt A vector (x, y) representing the point to test
	 * @return The shortest vector from this rectangle to the point
	 */
	public Vec2 shortestVectorToPoint(Vec2 pt)
	{
		Vec2 hsides = new Vec2(width, 0.0);
		Vec2 vsides = new Vec2(0.0, height);
		
		double x_on_hsides = x + new Vec2(pt.x - x, pt.y - y).projectOnto(hsides).x;
		double y_on_vsides = y + new Vec2(pt.x - x, pt.y - y).projectOnto(vsides).y;
		
		Vec2 from_left;
		Vec2 from_right;
		Vec2 from_top;
		Vec2 from_bottom;
		
		if (y_on_vsides < y)
		{
			from_left = new Vec2(pt.x - x, pt.y - y);
			from_right = new Vec2(pt.x - (x + width), pt.y - y);
		}
		else if (y_on_vsides > (y + height))
		{
			from_left = new Vec2(pt.x - x, pt.y - (y + height));
			from_right = new Vec2(pt.x - (x + width), pt.y - (y + height));
		}
		else
		{
			from_left = new Vec2(pt.x - x, pt.y - y_on_vsides);
			from_right = new Vec2(pt.x - (x + width), pt.y - y_on_vsides);
		}
		
		if (x_on_hsides < x)
		{
			from_top = new Vec2(pt.x - x, pt.y - (y + height));
			from_bottom = new Vec2(pt.x - x, pt.y - y);
		}
		else if (x_on_hsides > (x + width))
		{
			from_top = new Vec2(pt.x - (x + width), pt.y - (y + height));
			from_bottom = new Vec2(pt.x - (x + width), pt.y - y);
		}
		else
		{
			from_top = new Vec2(pt.x - x_on_hsides, pt.y - (y + height));
			from_bottom = new Vec2(pt.x - x_on_hsides, pt.y - y);
		}
		
		ArrayList<Vec2> vectors = new ArrayList<Vec2>();
		vectors.add(from_left);
		vectors.add(from_right);
		vectors.add(from_top);
		vectors.add(from_bottom);
		
		return Collections.min(vectors);
	}
	
	/**
	 * Get a string representation of the rectangle. The string will have the form
	 * of "Rect:{x:<X>, y:<Y>, width:<W>, height:<H>}"
	 * 
	 * @return String representation of the rectangle
	 */
	@Override
	public String toString()
	{
		return "Rect{x:" + x + ", y:" + y + ", width:" + width + ", height:" + height + "}";
	}
	
	/**
	 * Determine whether this rectangle is equal to another object.
	 * 
	 * @return True if the other object is a rectangle with exactly 
	 * 			the same size and coordinates, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Rect))
		{
			return false;
		}
		else
		{
			Rect o = (Rect)other;
			return (x == o.x && y == o.y && width == o.width && height == o.height);
		}
	}
	
	/**
	 * Draw the rectangle. Not intended for production.
	 * 
	 * @param g Slick2D Graphics to draw on / with
	 * @param ct Coordinate transformer
	 */
	public void debugDraw(Graphics g, CoordinateTransformer ct)
	{
		g.setLineWidth(2.0f);
		g.setColor(new Color(0.0f, 0.0f, 1.0f));
		g.drawRect((float)ct.toScreenX(x), (float)ct.toScreenY(y + height), (float)ct.widthToScreen(width), (float)ct.heightToScreen(height));
	}
}
