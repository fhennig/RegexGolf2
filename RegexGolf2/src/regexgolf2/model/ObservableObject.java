package regexgolf2.model;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public abstract class ObservableObject
{
	private final List<ObjectChangedListener> _listeners = new ArrayList<>();
	
	
	
	public void addObjectChangedListener(ObjectChangedListener listener)
	{
		_listeners.add(listener);
	}
	
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
