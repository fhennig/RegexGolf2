package regexgolf2.model.containers;

import java.util.EventObject;
import java.util.Optional;

import com.google.java.contract.Ensures;

@SuppressWarnings("serial")
public class ContainerChangedEvent<T> extends EventObject
{
	private final Optional<T> _addedItem;
	private final Optional<T> _removedItem;



	/**
	 * Creates a new ContainerChangedEvent
	 * 
	 * @param source
	 *            the source of the event
	 * @param addedItem
	 *            the item that was added, or null if no item was added
	 * @param removedItem
	 *            the item that was removed, or null if no item was removed
	 */
	public ContainerChangedEvent(Object source, T addedItem, T removedItem)
	{
		super(source);
		_addedItem = Optional.ofNullable(addedItem);
		_removedItem = Optional.ofNullable(removedItem);
	}



	@Ensures("result != null")
	public Optional<T> getAddedItem()
	{
		return _addedItem;
	}

	@Ensures("result != null")
	public Optional<T> getRemovedItem()
	{
		return _removedItem;
	}
}
