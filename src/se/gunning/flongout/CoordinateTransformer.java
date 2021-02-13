package se.gunning.flongout;

/**
 * CoordinateTransformer handles converting between world and screen coordinates.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-11
 */
public class CoordinateTransformer
{
	/**
	 * Screen width
	 */
	private double sw;
	
	/**
	 * Screen height
	 */
	private double sh;
	
	/**
	 * Screen offset X
	 */
	private double offsetX;
	
	/**
	 * Screen offset Y
	 */
	private double offsetY;
	
	/**
	 * Minimum X in world coordinates
	 */
	private double xmin;
	
	/**
	 * Maximum X in world coordinates
	 */
	private double xmax;
	
	/**
	 * Minimum Y in world coordinates
	 */
	private double ymin;
	
	/**
	 * Maximum Y in world coordinates
	 */
	private double ymax;
	
	/**
	 * Create a new coordinate transformer.
	 * 
	 * @param sw Screen height
	 * @param sh Screen width
	 * @param xmin Minimum X in world coordinates
	 * @param xmax Maximum X in world coordinates
	 * @param ymin Minimum Y in world coordinates
	 * @param ymax Maximum Y in world coordinates
	 */
	public CoordinateTransformer(double sw, double sh, double offsetX, double offsetY, double xmin, double xmax, double ymin, double ymax)
	{
		this.sw = sw;
		this.sh = sh;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	
	/**
	 * Transform a vector from world to screen coordinates.
	 * 
	 * @param v Vector in world coordinates
	 * @return Vector in screen coordinates
	 */
	public Vec2 toScreen(Vec2 v)
	{
		return new Vec2(offsetX + (v.x - xmin)/(xmax - xmin) * sw, offsetY + (1 - (v.y - ymin)/(ymax - ymin)) * sh);
	}
	
	/**
	 * Transform the X component of a vector from world to screen coordinates.
	 * 
	 * @param v Vector in world coordinates
	 * @return X component in screen coordinates
	 */
	public float toScreenX(Vec2 v)
	{
		return (float)offsetX + (float)((v.x - xmin)/(xmax - xmin) * sw);
	}
	
	/**
	 * Transform the Y component of a vector from world to screen coordinates.
	 * 
	 * @param v Vector in world coordinates
	 * @return Y component in screen coordinates
	 */
	public float toScreenY(Vec2 v)
	{
		return (float)offsetY + (float)((1 - (v.y - ymin)/(ymax - ymin)) * sh);
	}
	
	/**
	 * Transform an X coordinate from world to screen coordinates.
	 * 
	 * @param x X in world coordinates
	 * @return X in screen coordinates
	 */
	public float toScreenX(double x)
	{
		return (float)offsetX + (float)((x - xmin)/(xmax - xmin) * sw);
	}
	
	/**
	 * Transform an Y coordinate from world to screen coordinates.
	 * 
	 * @param y Y in world coordinates
	 * @return Y in screen coordinates
	 */
	public float toScreenY(double y)
	{
		return (float)offsetY + (float)((1 - (y - ymin)/(ymax - ymin)) * sh);
	}
	
	/**
	 * Transform a width from world to screen coordinates.
	 * 
	 * @param width Width in world coordinates
	 * @return Width in screen coordinates
	 */
	public float widthToScreen(double width)
	{
		return (float)(width * sw / (xmax - xmin));
	}
	
	/**
	 * Transform a height from world to screen coordinates.
	 * 
	 * @param width Height in world coordinates
	 * @return Height in screen coordinates
	 */
	public float heightToScreen(double height)
	{
		return (float)(height * sh / (ymax - ymin));
	}
	
	/**
	 * Transform a vector in screen coordinates to world coordinates.
	 * 
	 * @param v Vector in screen coordinates
	 * @return Vector in world coordinates
	 */
	public Vec2 toWorld(Vec2 v)
	{
		return new Vec2((v.x / sw) * (xmax - xmin) + xmin, (1 - (v.y / sh)) * (ymax - ymin) + ymin);
	}
	
	public float getOffsetX()
	{
		return (float)offsetX;
	}
	
	public float getOffsetY()
	{
		return (float)offsetY;
	}
}
