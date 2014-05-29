package regexgolf2.services.challengerepository;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class TrackableObject
{
	private final List<ObjectChangedListener> _changeListeners = new ArrayList<>();
	private int _id;
	
	void addObjectChangedListener(ObjectChangedListener listener)
	{
		_changeListeners.add(listener);
	}
	
	public int getId()
	{
		return _id;
	}
	
	void setId(int newId)
	{
		_id = newId;
	}
	
	protected final void fireObjectChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (ObjectChangedListener listener : _changeListeners)
		{
			listener.objectChanged(event);
		}
	}
}
