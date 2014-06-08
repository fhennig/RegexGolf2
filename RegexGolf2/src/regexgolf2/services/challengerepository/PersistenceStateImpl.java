package regexgolf2.services.challengerepository;

import java.util.EventObject;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.ObservableObject;

public class PersistenceStateImpl extends ObservableObject implements PersistenceState
{
	private final ObservableObject _object;
	private boolean _isNew = false;
	private boolean _isChanged = false;
	
		
	
	public PersistenceStateImpl(ObservableObject object, boolean isNew)
	{
		setNew();
		_object = object;
		initListener();
	}
	
	
	
	private void initListener()
	{
		_object.addObjectChangedListener(new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				if (_object.equals(event.getSource()))
					throw new IllegalArgumentException();
				
				reactToObjectChanged();
			}
		});
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
}
