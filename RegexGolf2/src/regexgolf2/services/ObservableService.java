package regexgolf2.services;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public abstract class ObservableService
{
	private final List<ServiceChangedListener> _listeners = new ArrayList<>();
	
	
	
	public void addServiceChangedListener(ServiceChangedListener listener)
	{
		_listeners.add(listener);
	}
	
	public void removeServiceChangedListener(ServiceChangedListener listener)
	{
		_listeners.remove(listener);
	}
	
	protected void fireServiceChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (ServiceChangedListener listener : _listeners)
		{
			listener.serviceChanged(event);
		}
	}
}
