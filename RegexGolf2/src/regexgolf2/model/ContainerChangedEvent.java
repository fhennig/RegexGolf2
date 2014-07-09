package regexgolf2.model;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ContainerChangedEvent<T> extends EventObject
{
	private final T _addedItem;
	private final T _removedItem;



	public ContainerChangedEvent(Object source, T addedItem, T removedItem)
	{
		super(source);
		_addedItem = addedItem;
		_removedItem = removedItem;
	}



	public T getAddedItem()
	{
		return _addedItem;
	}

	public T getRemovedItem()
	{
		return _removedItem;
	}
}
