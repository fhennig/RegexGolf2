package regexgolf2.services.persistence.changetracking;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.ObservableObject;
import regexgolf2.model.ObservableObjectImpl;

import com.google.java.contract.Requires;

public class PersistenceStateImpl extends ObservableObjectImpl implements PersistenceState
{
	private final ObservableObject _observedObject;
	private boolean _isNew = false;
	private boolean _isChanged = false;
	private final ObjectChangedListener _listener;
	
		
	
	@Requires("object != null")
	public PersistenceStateImpl(ObservableObject object, boolean isNew)
	{
		if (isNew)
			setNew();
		_observedObject = object;
		_listener = e -> reactToObjectChanged();
		_observedObject.addObjectChangedListener(_listener);
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
	
	public void dispose()
	{
		_observedObject.removeObjectChangedListener(_listener);
	}
}
