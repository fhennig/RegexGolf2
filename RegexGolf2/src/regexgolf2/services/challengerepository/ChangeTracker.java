package regexgolf2.services.challengerepository;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.google.java.contract.Requires;


public class ChangeTracker
{
	private final List<TrackedObjectChangedListener> _listeners = new ArrayList<>();
	private final TrackableObject _trackedObject;
	private boolean _isChanged;
	private boolean _isNew;
	
	
	
	@Requires("object != null")
	public ChangeTracker(TrackableObject object, boolean isChanged, boolean isNew)
	{
		_trackedObject = object;
		_isChanged = isChanged;
		_isNew = isNew;
		
		registerChangeListener();
	}
	
	
	
	private void registerChangeListener()
	{
		_trackedObject.addObjectChangedListener(new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				if (event.getSource() != _trackedObject)
					throw new IllegalArgumentException();
				
				objectChangedEvent();
			}
		});
	}
	
	private void objectChangedEvent()
	{
		if (_isChanged == true)
			return;
					
		_isChanged = true;
		fireTrackedObjectChangedEvent();
	}
	
	void changesSaved()
	{
		if (!_isNew && !_isChanged)
			return;
		
		_isNew = false;
		_isChanged = false;
		fireTrackedObjectChangedEvent();
	}
	
	public boolean objectIsChanged()
	{
		return _isChanged;
	}
	
	public boolean objectIsNew()
	{
		return _isNew;
	}
	
	private void fireTrackedObjectChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (TrackedObjectChangedListener listener : _listeners)
		{
			listener.trackedObjectChanged(event);
		}
	}
	
	@Requires("listener != null")
	public void addTrackedObjectChangedListener(TrackedObjectChangedListener listener)
	{
		_listeners.add(listener);
	}
}
