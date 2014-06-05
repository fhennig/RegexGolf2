package regexgolf2.services.persistence;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class IdWrapper<T>
{
	private final int _id;
	private final T _item;
	
	
	
	@Requires("item != null")
	public IdWrapper(int id, T item)
	{
		_id = id;
		_item = item;
	}
	
	
	
	public int getId()
	{
		return _id;
	}
	
	@Ensures("result != null")
	public T getItem()
	{
		return _item;
	}
}
