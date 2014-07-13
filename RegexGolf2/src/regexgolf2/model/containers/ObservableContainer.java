package regexgolf2.model.containers;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.ObservableObject;

/**
 * Adds a simple ObjectChanged Event to the Container class.
 */
public class ObservableContainer<T> extends Container<T> implements ObservableObject
{
	private final List<ObjectChangedListener> _listeners = new ArrayList<>();



	@Override
	public void addObjectChangedListener(ObjectChangedListener listener)
	{
		_listeners.add(listener);
	}

	@Override
	public void removeObjectChangedListener(ObjectChangedListener listener)
	{
		_listeners.remove(listener);
	}

	protected void fireObjectChangedEvent()
	{
		EventObject event = new EventObject(this);

		for (ObjectChangedListener listener : _listeners)
		{
			listener.objectChanged(event);
		}
	}
}
