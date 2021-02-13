package se.gunning.flongout;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;

/**
 * An immutable two-dimensional vector.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-11
 */
public class Vec2 implements Comparable<Vec2>
{
	/**
	 * First vector component.
	 */
	public final double x;
	
	/**
	 * Second vector component.
	 */
	public final double y;
	
	/**
	 * Create a new vector.
	 * 
	 * @param x First vector component
	 * @param y Second vector component
	 */
	public Vec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Create a new vector.
	 * 
	 * @param x First vector component
	 * @param y Second vector component
	 */
	public Vec2(int x, int y)
	{
		this.x = (double)x;
		this.y = (double)y;
	}
	
	/**
	 * Get a string representation of the vector. The representation takes the
	 * form of "(x, y)".
	 * 
	 * @return String representation of the vector
	 */
	@Override
	public String toString()
	{
		return "(" + this.x + ", " + this.y + ")";
	}
	
	/**
	 * Compute the dot product of this vector with a given vector.
	 * 
	 * @param v The other vector
	 * @return The dot product
	 */
	public double dot(Vec2 v)
	{
		return (x * v.x) + (y * v.y);
	}
	
	/**
	 * Get the length / Euclidean norm of this vector.
	 * 
	 * @return The length / Euclidean norm of this vector.
	 */
	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Create a new vector by taking the sum of this vector and a given vector.
	 * 
	 * @param other Vector to add
	 * @return The sum of the vectors
	 */
	public Vec2 add(Vec2 other)
	{
		return new Vec2(x + other.x, y + other.y);
	}
	
	/**
	 * Create a new vector by taking the difference of this vector and a given vector.
	 * 
	 * @param other Vector to subtract
	 * @return The difference of the vectors
	 */
	public Vec2 subtract(Vec2 other)
	{
		return new Vec2(x - other.x, y - other.y);
	}
	
	/**
	 * Create a new vector by scaling (multiplying) this vector by a scalar multiple.
	 * 
	 * @param scalar Factor to scale/multiply by
	 * @return The scaled vector
	 */
	public Vec2 scale(double scalar)
	{
		return new Vec2(x * scalar, y * scalar);
	}
	
	/**
	 * Create a new vector by normalizing this vector. The new vector will have a length
	 * of 1.0
	 * 
	 * @return The normalized vector
	 */
	public Vec2 normalize()
	{
		double len = length();
		
		if (len != 0.0)
		{
			return new Vec2(x / len, y / len);
		}
		else
		{
			return new Vec2(0, 0);
		}
	}
	
	/**
	 * Get the angle of this vector.
	 * 
	 * @return The angle of this vector
	 */
	public double getAngle()
	{
		return Math.atan2(y, x);
	}
	
	/**
	 * Create a new vector with the same length as this vector, but with a
	 * potentially different angle.
	 * 
	 * @param radians Angle of the new vector
	 * @return The new vector
	 */
	public Vec2 setAngle(double radians)
	{
		return new Vec2(Math.cos(radians) * length(), Math.sin(radians) * length());
	}
	
	/**
	 * Create a new vector by rotating this vector by a given angle.
	 * 
	 * @param radians Angle to rotate by
	 * @return The new vector
	 */
	public Vec2 rotate(double radians)
	{
		double newAngle = getAngle() + radians;
		return setAngle(newAngle);
	}
	
	/**
	 * Compute the dot project of two vectors.
	 * 
	 * @param u First vector
	 * @param v Second vector
	 * @return The dot product
	 */
	public static double dot(Vec2 u, Vec2 v)
	{
		return (u.x * v.x) + (u.y * v.y);
	}
	
	/**
	 * Compute the projection of this vector onto a given vector.
	 * 
	 * @param v Vector to project onto
	 * @return The projection vector
	 */
	public Vec2 projectOnto(Vec2 v)
	{
		double coeff = dot(this, v)/dot(v, v);
		return new Vec2(v.x * coeff, v.y * coeff);
	}
	
	/**
	 * Compare the length of this vector with a given vector.
	 * 
	 * @param other Vector to compare with
	 * @return 1 if this vector is longer than the other, -1 otherwise.
	 */
	@Override
	public int compareTo(Vec2 other)
	{
		return (length() - other.length() > 0) ? 1 : -1;
	}
	
	/**
	 * Draw the vector. Not intended for production.
	 * 
	 * @param g Slick2D Graphics to draw on / with
	 * @param ct Coordinate transformer
	 * @param x0 Origin x
	 * @param y0 Origin y
	 */
	public void debugDraw(Graphics g, CoordinateTransformer ct, double x0, double y0)
	{
		g.setLineWidth(6.0f);
		g.setColor(new Color(1.0f, 1.0f, 0.0f));
		g.drawLine((float)ct.toScreenX(x0), (float)ct.toScreenY(y0), (float)ct.toScreenX(x0 + x), (float)ct.toScreenY(y0 + y));
	}
	
	/**
	 * Draw the vector. Not intended for production.
	 * 
	 * @param g Slick2D Graphics to draw on / with
	 * @param ct Coordinate transformer
	 * @param x0 Origin x
	 * @param y0 Origin y
	 * @param col Color to use
	 */
	public void debugDraw(Graphics g, CoordinateTransformer ct, double x0, double y0, Color col)
	{
		g.setLineWidth(6.0f);
		g.setColor(col);
		g.drawLine((float)ct.toScreenX(x0), (float)ct.toScreenY(y0), (float)ct.toScreenX(x0 + x), (float)ct.toScreenY(y0 + y));
	}
}
