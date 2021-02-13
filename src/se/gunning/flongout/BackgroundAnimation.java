package se.gunning.flongout;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * A cool animation to have in the background.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class BackgroundAnimation
{
	/**
	 * Beats per minute.
	 */
	private double bpm;
	
	/**
	 * Animation phase.
	 */
	private double phase;
	
	/**
	 * Create a new background animation with a given BPM.
	 * 
	 * @param beatsPerMinute Beats per minute (BPM)
	 */
	public BackgroundAnimation(double beatsPerMinute)
	{
		bpm = beatsPerMinute;
	}
	
	/**
	 * Render the animation.
	 * 
	 * @param g Slick2D Graphics
	 * @param ct Coordinate transformer
	 */
	public void render(Graphics g, CoordinateTransformer ct)
	{
		double radius;
		double slowphase;
		g.setLineWidth(3.0f);
		
		for (int i = 0; i < 24; ++i)
		{
			slowphase = phase / 10.0;
			radius = (double)i / 1.5;
			
			radius += 0.5 * Math.abs(Math.sin(phase));
			
			// do some color cycling
			g.setColor(new Color(
					(float)Math.abs(Math.sin(phase + (double)i/4.0)/(3*radius/4.0)),
					(float)Math.abs(Math.sin(phase + 2.0 + (double)i/4.0)/(3*radius/4.0)),
					(float)Math.abs(Math.sin(phase + 4.0 + (double)i/4.0)/(3*radius/4.0))));
			
			// draw some circles
			g.drawOval(
					ct.toScreenX(0.0 - radius + Math.cos(slowphase)/2.0*radius),
					ct.toScreenY(0.0 + radius + Math.sin(slowphase)/2.0*radius),
					ct.widthToScreen(2 * radius),
					ct.widthToScreen(2 * radius));
		}
		
		phase += 2.0 * Math.PI * 1.0/60.0 * bpm/120.0;
	}
}
