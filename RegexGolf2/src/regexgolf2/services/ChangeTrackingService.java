package regexgolf2.services;

import java.util.HashMap;
import java.util.Map;

import regexgolf2.model.ObservableObject;
import regexgolf2.services.repositories.PersistenceState;
import regexgolf2.services.repositories.PersistenceStateImpl;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChangeTrackingService
{
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
	}

	@Requires(
	{ "object != null" })
	@Ensures("!isTracked(object)")
	public void untrack(ObservableObject object)
	{
		PersistenceStateImpl ps = _persistenceStates.remove(object);
		if (ps != null)
			ps.dispose();
	}

	@Requires("object != null")
	public boolean isTracked(ObservableObject object)
	{
		return _persistenceStates.containsKey(object);
	}

	@Requires(
	{ "isTracked(object)", "object != null" })
	@Ensures("result != null")
	public PersistenceState getPersistenceState(ObservableObject object)
	{
		return _persistenceStates.get(object);
	}
}
