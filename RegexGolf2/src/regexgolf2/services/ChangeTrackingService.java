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



	@Requires(
	{ "!isTracked(object)", "object != null" })
	@Ensures("isTracked(object)")
	public void track(ObservableObject object)
	{
		track(object, true);
	}

	@Requires(
	{ "!isTracked(object)", "object != null" })
	@Ensures("isTracked(object)")
	public void track(ObservableObject object, boolean isNew)
	{
		PersistenceStateImpl ps = new PersistenceStateImpl(object, isNew);
		_persistenceStates.put(object, ps);
	}

	@Requires(
	{ "isTracked(object)", "object != null" })
	@Ensures("!isTracked(object)")
	public void untrack(ObservableObject object)
	{
		_persistenceStates.remove(object);
		// TODO: call something like PersistenceState.dispose to unbind
		// Currently, this is not done, because persistencestates are only
		// disposed, if their object gets disposed. With newly introduced
		// uncoupling, this is may not always be the case
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
