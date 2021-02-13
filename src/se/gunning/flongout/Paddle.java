package se.gunning.flongout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Represents the paddles used in the game.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class Paddle
{
	/**
	 * Position of the paddle.
	 */
	private Vec2 position;
	
	/**
	 * Velocity (translation) of the paddle.
	 */
	private Vec2 velocity;
	
	/**
	 * Direction (heading) of the paddle.
	 */
	private Vec2 direction;
	
	/**
	 * Angular velocity of the paddle.
	 */
	private double angularVelocity;
	
	/**
	 * Radius of the butt of the paddle.
	 */
	private double buttRadius;
	
	/**
	 * Radius of the tip of the paddle.
	 */
	private double tipRadius;
	
	/**
	 * Distance between the center points of the butt and tip.
	 */
	private double length;
	
	/**
	 * Scale of the paddle.
	 */
	private double scale;
	
	/**
	 * Resting angle of the paddle.
	 */
	private double restingAngle;
	
	/**
	 * Area in which the paddle can move.
	 */
	private Rect area;
	
	/**
	 * Name assigned to the paddle.
	 */
	private String name;
	
	// TODO: is this in use anymore?
	/**
	 * Graphic for the paddle.
	 */
	Image paddle;
	
	/**
	 * Create a new paddle.
	 * 
	 * @param scale Scale of the paddle
	 * @param restingAngle Resting angle of the paddle
	 */
	public Paddle(double scale, double restingAngle)
	{
		setScale(scale);
		this.restingAngle = restingAngle;
		
		// set a default area
		area = new Rect(-100.0, -100.0, 200.0, 200.0);
	}
	
	/**
	 * Scale the paddle. The scaling applies to the radius of the
	 * butt and tip and the distance between them.
	 * 
	 * @param scale Scale to apply
	 */
	public void setScale(double scale)
	{
		this.scale = scale;
		
		buttRadius = 0.3 * scale;
		tipRadius = 0.1 * scale;
		length = 1.5 * scale; 
	}
	
	/**
	 * Get the angular velocity (speed of rotation) of the paddle.
	 *   
	 * @return The angular velocity of the paddle in radians per unit time
	 */
	public double getAngularVelocity()
	{
		return angularVelocity;
	}
	
	/**
	 * Set the angular velocity (speed of rotation) of the paddle.
	 * 
	 * @param radians New angular velocity in radians per unit time
	 */
	public void setAngularVelocity(double radians)
	{
		angularVelocity = radians;
	}
	
	/**
	 * Set the area of possible movement for the paddle.
	 * 
	 * @param area New area of movement
	 */
	public void setArea(Rect area)
	{
		this.area = area;
	}
	
	/**
	 * Get the velocity (of translation) of the paddle.
	 * 
	 * @return Velocity of the paddle
	 */
	public Vec2 getVelocity()
	{
		return velocity;
	}
	
	/**
	 * Set the velocity (of translation) of the paddle.
	 * 
	 * @param v New velocity vector
	 */
	public void setVelocity(Vec2 v)
	{
		velocity = v;
	}
	
	/**
	 * Set the position of the butt of the paddle.
	 * 
	 * @param v Position vector to set
	 */
	public void setPosition(Vec2 v)
	{
		position = v;
	}
	
	/**
	 * Get the position of the butt of the paddle.
	 * 
	 * @return Position of the butt of the paddle
	 */
	public Vec2 getPosition()
	{
		return position;
	}
	
	/**
	 * Set the direction (heading) of the paddle.
	 * 
	 * @param v Direction vector to set
	 */
	public void setDirection(Vec2 v)
	{
		direction = v;
	}
	
	/**
	 * Get the direction (heading) of the paddle.
	 * 
	 * @return Direction vector of the paddle
	 */
	public Vec2 getDirection()
	{
		return direction;
	}
	
	/**
	 * Set the angle between the butt and tip of the paddle.
	 * 
	 * @param radians Angle in radians
	 */
	public void setAngle(double radians)
	{
		direction = new Vec2(Math.cos(radians), Math.sin(radians));
//		// TODO: this doesn't look right, why is there a conditional here at all?
//		if (Math.abs(radians) < 1e-10)
//		{
//			direction = new Vec2(Math.cos(restingAngle), Math.sin(restingAngle));
//		}
//		else
//		{
//			direction = new Vec2(Math.cos(radians), Math.sin(radians));
//		}
	}
	
	/**
	 * Get the angle between the butt and tip of the paddle.
	 * 
	 * @return
	 */
	public double getAngle()
	{
		return Math.atan2(direction.y, direction.x);
	}
	
	/**
	 * Get the resting angle between the butt and tip of the paddle.
	 * 
	 * @return Resting angle of the paddle
	 */
	public double getRestingAngle()
	{
		return restingAngle;
	}
	
	/**
	 * Get the radius of the butt of the paddle.
	 * 
	 * @return Radius of paddle butt
	 */
	public double getButtRadius()
	{
		return buttRadius;
	}
	
	/**
	 * Get the radius of the tip of the paddle.
	 * 
	 * @return Radius of tip of paddle
	 */
	public double getTipRadius()
	{
		return tipRadius;
	}
	
	/**
	 * Get the distance between the center points of the butt
	 * and tip of the paddle.
	 *  
	 * @return Length of the paddle
	 */
	public double getLength()
	{
		return length;
	}
	
	/**
	 * Get the position of the tip of the paddle.
	 * 
	 * @return Position of the tip of the paddle
	 */
	public Vec2 getTipPosition()
	{
		return position.add(direction.scale(length));
	}
	
	/**
	 * Get the shortest vector from the edge of the paddle to a
	 * given point.
	 * 
	 * @param pt Point to consider
	 * @return The shortest vector to the given point
	 */
	public Vec2 getShortestVectorToPoint(Vec2 pt)
	{
		ArrayList<Vec2> vectors = new ArrayList<Vec2>();
		
		Vec2 tip = getTipPosition();
		
		Vec2 tiptop = new Vec2(tip.x + Math.cos(getAngle() + Math.PI / 2.0) * tipRadius, tip.y + Math.sin(getAngle() + Math.PI / 2.0) * tipRadius);
		Vec2 tipbottom =new Vec2(tip.x + Math.cos(getAngle() - Math.PI / 2.0) * tipRadius, tip.y + Math.sin(getAngle() - Math.PI / 2.0) * tipRadius);
		
		Vec2 butttop = new Vec2(position.x + Math.cos(getAngle() + Math.PI / 2.0) * buttRadius, position.y + Math.sin(getAngle() + Math.PI / 2.0) * buttRadius);
		Vec2 buttbottom =new Vec2(position.x + Math.cos(getAngle() - Math.PI / 2.0) * buttRadius, position.y + Math.sin(getAngle() - Math.PI / 2.0) * buttRadius);
		
		Vec2 topedge = new Vec2(tiptop.x - butttop.x, tiptop.y - butttop.y);
		Vec2 bottomedge = new Vec2(tipbottom.x - buttbottom.x, tipbottom.y - buttbottom.y);
		
		Vec2 topcenter = new Vec2(butttop.x + topedge.x / 2.0, butttop.y + topedge.y / 2.0);
		Vec2 projtop = new Vec2(pt.x - topcenter.x, pt.y - topcenter.y).projectOnto(topedge);
		
		Vec2 bottomcenter = new Vec2(buttbottom.x + bottomedge.x / 2.0, buttbottom.y + bottomedge.y / 2.0);
		Vec2 projbottom = new Vec2(pt.x - bottomcenter.x, pt.y - bottomcenter.y).projectOnto(bottomedge);
		
		if (projtop.length() < topedge.length() / 2.0)
		{
			vectors.add(new Vec2(pt.x - topcenter.x, pt.y - topcenter.y).subtract(projtop));
		}
		
		if (projbottom.length() < bottomedge.length() / 2.0)
		{
			vectors.add(new Vec2(pt.x - bottomcenter.x, pt.y - bottomcenter.y).subtract(projbottom));
		}
		
		Vec2 from_tip = new Vec2(pt.x - tip.x, pt.y - tip.y);
		from_tip = new Vec2(pt.x - (tip.x + Math.cos(from_tip.getAngle()) * tipRadius), pt.y - (tip.y + Math.sin(from_tip.getAngle()) * tipRadius));
		
		Vec2 from_butt = new Vec2(pt.x - position.x, pt.y - position.y);
		from_butt = new Vec2(pt.x - (position.x + Math.cos(from_butt.getAngle()) * buttRadius), pt.y - (position.y + Math.sin(from_butt.getAngle()) * buttRadius));
		
		vectors.add(from_tip);
		vectors.add(from_butt);
		
		return Collections.min(vectors); 
	}
	
	/**
	 * Draw the vectors and circles that define the paddle.
	 * Not intended for production.
	 * 
	 * @param g Slick2D Graphics
	 * @param ct Coordinate transformer
	 */
	public void debugDraw(Graphics g, CoordinateTransformer ct)
	{
		g.setLineWidth(4.0f);
		g.setColor(new Color(1.0f, 1.0f, 0.0f));
		g.drawOval((float)ct.toScreenX(position.x), (float)ct.toScreenY(position.y), 2.0f, 2.0f);
		
		g.setColor(new Color(1.0f, 0.0f, 0.0f));
		g.drawOval((float)ct.toScreenX(position.x - buttRadius), (float)ct.toScreenY(position.y + buttRadius), (float)ct.widthToScreen(2.0*buttRadius), (float)ct.widthToScreen(2.0*buttRadius));
		
		Vec2 tip = position.add(direction.scale(length));
		
		g.setColor(new Color(0.0f, 1.0f, 0.0f));
		g.drawOval((float)ct.toScreenX(tip.x - tipRadius), (float)ct.toScreenY(tip.y + tipRadius), (float)ct.widthToScreen(2*tipRadius), (float)ct.widthToScreen(2*tipRadius));
		
		g.setColor(new Color(1.0f, 1.0f, 0.0f));
		
		float x1, y1, x2, y2;
		
		x1 = (float)ct.toScreenX(position.x + buttRadius * Math.cos(getAngle() - Math.PI / 2.0));
		y1 = (float)ct.toScreenY(position.y + buttRadius * Math.sin(getAngle() - Math.PI / 2.0 ));
		
		x2 = (float)ct.toScreenX(tip.x + tipRadius * Math.cos(getAngle() - Math.PI / 2.0));
		y2 = (float)ct.toScreenY(tip.y + tipRadius * Math.sin(getAngle() - Math.PI / 2.0 ));
		
		g.drawLine(x1, y1, x2, y2);
		
		x1 = (float)ct.toScreenX(position.x + buttRadius * Math.cos(getAngle() + Math.PI / 2.0));
		y1 = (float)ct.toScreenY(position.y + buttRadius * Math.sin(getAngle() + Math.PI / 2.0 ));
		
		x2 = (float)ct.toScreenX(tip.x + tipRadius * Math.cos(getAngle() + Math.PI / 2.0));
		y2 = (float)ct.toScreenY(tip.y + tipRadius * Math.sin(getAngle() + Math.PI / 2.0 ));
		
		g.drawLine(x1, y1, x2, y2);
	}
	
	/**
	 * Get the coordinate of the top left corner of the rectangle that
	 * contains the butt of the paddle.
	 * 
	 * @return Top left corner of rectangle that contains butt of paddle
	 */
	public Vec2 getTopLeftCorner()
	{
		return new Vec2(position.x - getWidth() / 2.0f, position.y + getHeight() / 2.0f);
	}
	
	// TODO: is this in use anymore?
	/**
	 * Set the graphic for the paddle.
	 * 
	 * @param image Filename to load
	 * @throws FileNotFoundException
	 * @throws SlickException
	 */
	public void setGraphic(String image) throws FileNotFoundException, SlickException
	{
		paddle = new Image(new FileInputStream(image), "paddle", false);
		paddle = paddle.getScaledCopy(0.2f);
	}
	
	/**
	 * Get the total width of the paddle, including the radii of the
	 * butt and tip at zero rotation.
	 * 
	 * @return Total width of the paddle at zero rotation
	 */
	public double getWidth()
	{
		return buttRadius + length + tipRadius;
	}
	
	/**
	 * Get the maximum height of the paddle at zero rotation, which
	 * is to say the diameter of the butt of the paddle.
	 * 
	 * @return The maximum height of the paddle at zero rotation
	 */
	public double getHeight()
	{
		return buttRadius*2;
	}
	
	// TODO: is this in use anymore?
	/**
	 * Get the graphic for the paddle.
	 * 
	 * @return Graphic for the paddle
	 */
	public Image getGraphic()
	{
		return paddle;
	}
	
	/**
	 * Move the paddle.
	 * 
	 * @param x Horizontal movement
	 * @param y Vertical movement
	 */
	public void move(double x, double y)
	{
		move(new Vec2(x, y));
	}
	
	/**
	 * Move the paddle.
	 * 
	 * @param v Movement vector
	 */
	public void move(Vec2 v)
	{
		position = new Vec2(position.x + v.x, position.y + v.y);
		
		// don't go outside of alloted area
		if (!area.containsPoint(position))
		{
			move(area.shortestVectorToPoint(position).scale(-1.0));
		}
	}
	
	/**
	 * Rotate towards a given angle by applying angular velocity.
	 * 
	 * @param angle Target angle
	 * @param angularAcceleration Speed of rotation
	 */
	public void rotateTowardsAngle(double angle, double angularAcceleration)
	{
		double myangle = getAngle();
		
		double anglediff = angle - myangle;
		double anglediff2 = angle + 2.0 * Math.PI - myangle;
		
		if (Math.abs(anglediff2) < Math.abs(anglediff))
		{
			anglediff = anglediff2;
		}
		
		double anglediff3 = angle - myangle - 2.0 * Math.PI;
		
		if (Math.abs(anglediff3) < Math.abs(anglediff))
		{
			anglediff = anglediff3;
		}
		
		setAngularVelocity(angularAcceleration * anglediff);
	}
	
	/**
	 * Get the scale of the paddle.
	 * 
	 * @return Scale of the paddle
	 */
	public double getScale()
	{
		return scale;
	}
	
	/**
	 * Assign a name to the paddle.
	 * 
	 * @param name Name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Get the name assigned to this paddle.
	 * 
	 * @return Name of this paddle
	 */
	public String getName()
	{
		return name;
	}
}
