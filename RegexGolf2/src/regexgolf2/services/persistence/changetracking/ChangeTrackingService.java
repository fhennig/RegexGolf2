package regexgolf2.services.persistence.changetracking;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import regexgolf2.model.ObservableObject;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChangeTrackingService implements PersistenceInformation
{
	private static final Logger _LOG = Logger.getLogger(ChangeTrackingService.class.getName());
	private final Map<ObservableObject, PersistenceStateImpl> _persistenceStates = new HashMap<>();



	@Requires("object != null")
	@Ensures("isTracked(object)")
	public void track(ObservableObject object)
	{
		track(object, true);
	}

	@Requires("object != null")
	@Ensures("isTracked(object)")
	public void track(ObservableObject object, boolean isNew)
	{
		if (isTracked(object))
			return;
		PersistenceStateImpl ps = new PersistenceStateImpl(object, isNew);
		_persistenceStates.put(object, ps);
		_LOG.info(object + " is new being tracked. isNew=" + isNew);
	}

	@Requires(
	{ "object != null" })
	@Ensures("!isTracked(object)")
	public void untrack(ObservableObject object)
	{
		if (!_persistenceStates.containsKey(object))
			return;
		PersistenceStateImpl ps = _persistenceStates.remove(object);
		ps.dispose();
		_LOG.info(object + " is now untracked.");
	}
	
	@Requires("isTracked(object)")
	public void objectWasPersisted(ObservableObject object)
	{
		_persistenceStates.get(object).objectWasPersisted();
	}

	@Override
	public PersistenceState getPersistenceState(ObservableObject object)
	{
		return _persistenceStates.get(object);
	}

	@Override
	public boolean isNew(ObservableObject object)
	{
		return getPersistenceState(object).isNew();
	}

	@Override
	public boolean isChanged(ObservableObject object)
	{
		return getPersistenceState(object).isChanged();
	}

	@Override
	public boolean isTracked(ObservableObject object)
	{
		return _persistenceStates.containsKey(object);
	}
}
