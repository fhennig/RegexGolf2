package regexgolf2.services.repositories;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.ObservableObject;

import com.google.java.contract.Requires;

public class PersistenceStateImpl extends ObservableObject implements PersistenceState
{
	private final ObservableObject _object;
	private boolean _isNew = false;
	private boolean _isChanged = false;
	private final ObjectChangedListener _listener;
	
		
	
	@Requires("object != null")
	public PersistenceStateImpl(ObservableObject object, boolean isNew)
	{
		if (isNew)
			setNew();
		_object = object;
		_listener = e -> reactToObjectChanged();
		_object.addObjectChangedListener(_listener);
	}
	
	
	
	private void reactToObjectChanged()
	{
		setChanged();
	}
	
	private void setNew()
	{
		if (_isNew == true)
			return;
		_isNew = true;
		_isChanged = true;
		fireObjectChangedEvent();
	}
	
	private void setChanged()
	{
		if (_isChanged == true)
			return;
		_isChanged = true;
		fireObjectChangedEvent();
	}
	
	/**
	 * Method that can be called to reset the persistenceState.
	 * It marks the object as unchanged and not new.
	 */
	public void objectWasPersisted()
	{
		if (!_isChanged && !_isNew)
			return;
		_isNew = false;
		_isChanged = false;
		fireObjectChangedEvent();
	}

	@Override
	public boolean isNew()
	{
		return _isNew;
	}

	@Override
	public boolean isChanged()
	{
		return _isChanged;
	}

	@Override
	public Object getObservedItem()
	{
		return _object;
	}
	
	public void dispose()
	{
		_object.removeObjectChangedListener(_listener);
	}
}
