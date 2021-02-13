package se.gunning.flongout;

/**
 * A simple counter.
 * 
 * @author Mikael Forsberg
 * @author Robin Gunning
 * @author Jonathan Yao Håkansson
 * @version 2015-05-11
 */
public class Counter
{
	/**
	 * The current count.
	 */
	private int count = 0;
	
	/**
	 * Create a new counter.
	 */
	public Counter()
	{
	}
	
	/**
	 * Set the count.
	 * 
	 * @param c New count
	 */
	public void setCount(int c)
	{
		count = c;
	}
	
	/**
	 * Add to the count.
	 * 
	 * @param c Number of counts to add
	 */
	public void addCount(int c)
	{
		count += c;
	}
	
	/**
	 * Increment the counter by one.
	 */
	public void incrementByOne()
	{
		++count;
	}
	
	/**
	 * Decrement the counter by one.
	 */
	public void decrementByOne()
	{
		--count;
	}
	
	/**
	 * Check whether or not the counter is at zero.
	 * 
	 * @return True if the counter is at zero, false otherwise
	 */
	public boolean isZero()
	{
		return count == 0;
	}

	/**
	 * Get current count.
	 * 
	 * @return Current count
	 */
	public int getCount()
	{
		return count;
	}
	
	/**
	 * Get current count as string.
	 * 
	 * @return Current count as string
	 */
	public String toString()
	{
		return "" + count;
	}
}
