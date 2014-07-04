package regexgolf2.util;

import java.util.Random;

import com.google.java.contract.Requires;

public final class Util
{
	private static final Random _RANDOM = new Random();

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 * @see <a href="http://stackoverflow.com/a/363692/3063148">Stackoverflow post</a>
	 */
	@Requires("min <= max")
	public static int randInt(int min, int max)
	{
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = _RANDOM.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
