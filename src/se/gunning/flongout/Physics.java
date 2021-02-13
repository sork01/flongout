package se.gunning.flongout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Simulates a small world with some physics-based interactions between certain objects.
 *  
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-12
 */
public class Physics
{
	/**
	 * Interface for collision callbacks.
	 * 
	 * @author Mikael Forsberg
	 * @author Robin Gunning
	 * @author Jonathan Yao Håkansson
	 * @version 2015-05-12
	 */
	public interface CollisionAction
	{
		/**
		 * Handle a ball-to-wall collision.
		 * 
		 * @param ball Ball that collided
		 * @param group Group that the wall belongs to
		 * @param wall Wall rectangle
		 */
		public void onCollision(Ball ball, Rect wall, String group, Physics world);
		public void onCollision(Ball ball, Paddle paddle, Physics world);
		public void onPaddleHit(Ball ball, Paddle paddle, Physics world);
	}
	
	private static class WallToRemove
	{
		public Rect wall;
		public String group;
		
		public WallToRemove(Rect wall, String group)
		{
			this.wall = wall;
			this.group = group;
		}
	}
	
	/**
	 * Walls
	 */
	private ArrayList<Rect> walls;
	
	private ArrayList<WallToRemove> wallsToRemove;
	
	/**
	 * Wall groups
	 */
	private HashMap<String, ArrayList<Rect>> wallGroups;
	
	/**
	 * Maximum divisor for timestep halving
	 */
	private static final double MAX_DIVISOR = 256;
	
	/**
	 * Acceleration of gravity
	 */
	private double gravity;
	
	/**
	 * Balls
	 */
	private ArrayList<Ball> balls;
	
	/**
	 * Paddles
	 */
	private ArrayList<Paddle> paddles;
	
	/**
	 * Create a new physics world / simulation.
	 */
	public Physics()
	{
		wallGroups = new HashMap<String, ArrayList<Rect>>();
		walls = new ArrayList<Rect>();
		wallsToRemove = new ArrayList<WallToRemove>();
		balls = new ArrayList<Ball>();
		paddles = new ArrayList<Paddle>();
		
		gravity = 0.0;
	}
	
	/**
	 * Add a ball to the simulation.
	 * 
	 * @param b Ball to add
	 */
	public void addBall(Ball b)
	{
		balls.add(b);
	}
	
	/**
	 * Remove a ball from the simulation.
	 * 
	 * @param b Ball to remove
	 */
	public void removeBall(Ball b)
	{
		balls.remove(b);
	}
	
	/**
	 * Get all balls in the simulation.
	 * 
	 * @return The balls
	 */
	public ArrayList<Ball> getBalls()
	{
		return balls;
	}
	
	/**
	 * Add a paddle to the simulation.
	 * 
	 * @param p Paddle to add
	 */
	public void addPaddle(Paddle p)
	{
		paddles.add(p);
	}
	
	/**
	 * Remove a paddle from the simulation.
	 * 
	 * @param p Paddle to remove
	 */
	public void removePaddle(Paddle p)
	{
		paddles.remove(p);
	}
	
	/**
	 * Get all paddles in the simulation.
	 * 
	 * @return The paddles
	 */
	public ArrayList<Paddle> getPaddles()
	{
		return paddles;
	}
	
	/**
	 * Set the acceleration of gravity
	 * 
	 * @param g New acceleration of gravity
	 */
	public void setGravity(double g)
	{
		gravity = g;
	}
	
	/**
	 * Get the acceleration of gravity
	 * 
	 * @return Acceleration of gravity
	 */
	public double getGravity()
	{
		return gravity;
	}
	
	/**
	 * Add a wall.
	 * 
	 * @param group Group identifier
	 * @param x X coordinate of bottom left corner
	 * @param y Y coordinate of bottom left corner
	 * @param width Width (grows to the right)
	 * @param height Height (grows upwards)
	 */
	public void addWall(String group, double x, double y, double width, double height)
	{
		addWall(group, new Rect(x, y, width, height));
	}
	
	/**
	 * Add a wall.
	 * 
	 * @param group Group identifier
	 * @param wall Rectangle to set as wall
	 */
	public void addWall(String group, Rect wall)
	{
		if (!wallGroups.containsKey(group))
		{
			wallGroups.put(group, new ArrayList<Rect>());
		}
		
		wallGroups.get(group).add(wall);
		walls.add(wall);
	}
	
	public void removeWall(String group, Rect wall)
	{
		wallsToRemove.add(new WallToRemove(wall, group));
	}
	
	/**
	 * Remove all walls belonging to a given group.
	 * 
	 * @param group Group identifier
	 */
	public void clearWallGroup(String group)
	{
		for (Rect wall : wallGroups.get(group))
		{
			walls.remove(wall);
		}
		
		wallGroups.get(group).clear();
	}
	
	public String getGroupForWall(Rect wall)
	{
		for (String group : wallGroups.keySet())
		{
			if (wallGroups.get(group).contains(wall))
			{
				return group;
			}
		}
		
		return null;
	}
	
	/**
	 * Run one iteration of the simulation with a given time step.
	 * Run one iteration of the simulation with a given time step and
	 * no collision action.
	 * 
	 * @param time Time step
	 */
	public void step(double time)
	{
		for (Ball b : balls)
		{
			step(b, time, 1, null);
		}
	}
	
	/**
	 * Run one iteration of the simulation with a given time step and
	 * a single collision action.
	 * 
	 * @param b Ball
	 * @param time Time step
	 * @param action collision actions to apply to any collisions
	 */
	public void step(double time, CollisionAction action)
	{
		for (Ball b : balls)
		{
			step(b, time, 1, new CollisionAction[]{action});
		}
	}
	
	/**
	 * Run one iteration of the simulation with a given time step and
	 * a set of collision actions.
	 * 
	 * @param time Time step
	 * @param actions Set of collision actions to apply to any collisions
	 */
	public void step(double time, CollisionAction[] actions)
	{
		for (Ball b : balls)
		{
			step(b, time, 1, actions);
		}
	}
	
	/**
	 * Run one iteration of the simulation with a given time step along
	 * with a divisor for the time step.
	 * 
	 * @param b Ball
	 * @param time Time step
	 * @param divisor Time step divisor
	 * @param actions Set of collision actions to apply
	 */
	private void step(Ball b, double time, double divisor, CollisionAction[] actions)
	{
		if (divisor > MAX_DIVISOR)
		{
			// TODO: squeeze ball or something
			
			b.setPosition(new Vec2(0, 0));
			return;
		}
		
		time /= divisor;
		
		Vec2 vel = b.getVelocity();
		
		b.move(vel.x * time, vel.y * time);
		
		int num_collision = 0;
		
		for (Rect w : walls)
		{
			if (w.distanceToPoint(b.getPosition()) < b.getRadius())
			{
				++num_collision;
			}
		}
		
		for (Paddle p : paddles)
		{
			if (p.getShortestVectorToPoint(b.getPosition()).length() < b.getRadius())
			{
				++num_collision;
			}
		}
		
		if (num_collision > 1)
		{
			// cut the time step in half in the hopes of only needing to handle one collision at a time
			b.move(vel.x * -time, vel.y * -time);
			step(b, time, divisor * 2.0, actions);
			step(b, time, divisor * 2.0, actions);
			return;
		}
		else if (num_collision == 1)
		{
			for (Rect w : walls)
			{
				Vec2 v = w.shortestVectorToPoint(b.getPosition());
				
				if (v.length() < b.getRadius())
				{
					// total distance of over-travel
					double d = (b.getRadius() - v.length());
					
					// project ball velocity vector onto collision vector
					// to get magnitude of component in collision direction
					Vec2 proj = vel.projectOnto(v);
					
					// time taken to do the over-travel
					double dt = d / proj.length();
					
					// rewind the move
					b.move(vel.x * -time, vel.y * -time);
					
					// move to the moment of impact
					b.move(vel.x * (time - dt), vel.y * (time - dt));
					
					// bounce
					double floor_angle = v.rotate(-Math.PI/2.0).getAngle();
					b.setVelocity(b.getVelocity().setAngle(floor_angle + (floor_angle - b.getVelocity().getAngle())));
					b.setVelocity(b.getVelocity().scale(0.95));
					
//					b.setVelocity(new Vec2(vel.x * ((Math.abs(v.x) > 1e-10 ? -1.0 : 1.0)), vel.y * ((Math.abs(v.y) > 1e-10 ? -1.0 : 1.0))));
					
					// move to fill remaining amount of requested time
					b.move(b.getVelocity().scale(dt));
					
					if (actions != null && actions.length > 0)
					{
						for (int i = 0; i < actions.length; ++i)
						{
							actions[i].onCollision(b, w, getGroupForWall(w), this);
						}
					}
				}
			}
			
			for (Paddle p : paddles)
			{
				Vec2 v = p.getShortestVectorToPoint(b.getPosition());
				
				if (v.length() < b.getRadius())
				{
					p.move(v.scale(-0.1));
					
					// TODO: (?) proper bounce
					
					// rewind the move
					b.move(vel.x * -time, vel.y * -time);
					
					// don't figure exact impact point, bounce right here instead 
					double floor_angle = v.rotate(-Math.PI/2.0).getAngle();
					b.setVelocity(b.getVelocity().setAngle(floor_angle + (floor_angle - b.getVelocity().getAngle())));
					b.setVelocity(b.getVelocity().scale(0.75));
					
					vel = b.getVelocity();
					
					b.move(vel.x * time, vel.y * time);
					
					if (actions != null && actions.length > 0)
					{
						for (int i = 0; i < actions.length; ++i)
						{
							actions[i].onCollision(b, p, this);
						}
					}
				}
			}
		}
		else
		{
			// try moving paddles
			for (Paddle p : paddles)
			{
				p.setAngle(p.getAngle() + p.getAngularVelocity() * time);
				
				Vec2 pos = b.getPosition();
				Vec2 v = p.getShortestVectorToPoint(pos);
				
				if (v.length() < b.getRadius())
				{
					p.move(v.scale(-0.1));
					
					// move ball away from paddle
					double d = b.getRadius() - v.length();
					Vec2 vv = v.normalize().scale(2.0 * d);
					// TODO: what if the ball collides with a wall here?
					b.move(vv.x, vv.y);
					
					// bounce the ball
					double floor_angle = v.rotate(-Math.PI/2.0).getAngle();
					b.setVelocity(b.getVelocity().setAngle(floor_angle + (floor_angle - b.getVelocity().getAngle())));
					
					// accelerate the ball
					Vec2 vpaddle = p.getTipPosition().subtract(p.getPosition());
					Vec2 bproj = b.getPosition().subtract(p.getPosition()).projectOnto(vpaddle);
					double hitoffset = bproj.length() / vpaddle.length();
					
					// hit further out than center of paddle tip, add less energy
					if (hitoffset > 1.0)
					{
						hitoffset = 2.0 - hitoffset;
					}
					
					b.accelerate(v.normalize().scale(2.0 * Math.abs(p.getAngularVelocity()) * hitoffset));
					
					// not sure whether to rewind this or not
					p.setAngle(p.getAngle() - p.getAngularVelocity() * time);
					
					if (actions != null && actions.length > 0)
					{
						for (int i = 0; i < actions.length; ++i)
						{
							actions[i].onPaddleHit(b, p, this);
						}
					}
				}
				
				// try to move (translate) the paddle
				p.move(p.getVelocity().scale(time));
				v = p.getShortestVectorToPoint(pos);
				
				if (v.length() < b.getRadius())
				{
					p.move(v.scale(-0.1));
					p.move(p.getVelocity().scale(-time));
				}
			}
		}
		
		b.accelerate(0.0, gravity * time);
		
		// maintain ball max velocity (is this a bad idea?)
		if (b.getVelocity().length() > 25.0)
		{
			b.setVelocity(b.getVelocity().normalize().scale(25.0));
		}
		
		for (WallToRemove wall : wallsToRemove)
		{
			if (wallGroups.get(wall.group) != null)
			{
				wallGroups.get(wall.group).remove(wall.wall);
			}
			
			walls.remove(wall.wall);
		}
		
		wallsToRemove.clear();
	}
}
