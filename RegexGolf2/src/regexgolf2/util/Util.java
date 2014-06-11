package regexgolf2.util;

import java.util.Collection;

public class Util
{
	/**
	 * Removes every Element in the target Collection that is not present 
	 * in the source Collection and 
	 * adds every Element from the source to the target that is missing in the target.
	 */
	public static <T> void synchronize(Collection<T> source, Collection<T> target)
	{
		//Remove all items from target that are not contained in the source.
		for (T item : target)
		{
			if (!source.contains(item))
				target.remove(item);
		}
		//Add all items to the target that are in source but not in target.
		for (T item : source)
		{
			if (!target.contains(item))
				target.add(item);
		}
	}
}
