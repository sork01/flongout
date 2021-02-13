package se.gunning.flongout;

/**
 * A simple stopwatch.
 *  
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-19
 */
public class Stopwatch
{
	/**
	 * Accumulated time.
	 */
	private long sum = 0;
	
	/**
	 * Time of starting the stopwatch.
	 */
	private long started = 0;
	
	/**
	 * Create a new stopwatch.
	 */
	public Stopwatch()
	{
	}
	
	/**
	 * Start the stopwatch. Does nothing if the stopwatch
	 * has already been started.
	 */
	public void start()
	{
		if (started == 0)
		{
			started = System.currentTimeMillis();
		}
	}
	
	/**
	 * Stop the stopwatch. Does nothing if the stopwatch
	 * hasn't been started.
	 */
	public void stop()
	{
		if (started != 0)
		{
			sum += System.currentTimeMillis() - started;
			started = 0;
		}
	}
	
	/**
	 * Get the accumulated time on the stopwatch.
	 * 
	 * @return Time on the stopwatch
	 */
	public long getSum()
	{
		if (started != 0)
		{
			return sum + System.currentTimeMillis() - started;
		}
		else
		{
			return sum;
		}
	}
	
	/**
	 * Get the accumulated time in seconds.
	 * 
	 * @return Accumulated time in seconds
	 */
	public long getSeconds()
	{
		return getSum() / 1000;
	}
	
	/**
	 * Reset the stopwatch.
	 */
	public void reset()
	{
		sum = 0;
		started = 0;
	}
}
