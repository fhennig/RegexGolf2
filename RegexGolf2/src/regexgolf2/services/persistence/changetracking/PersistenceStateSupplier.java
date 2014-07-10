package regexgolf2.services.persistence.changetracking;

import regexgolf2.model.ObservableObject;

public interface PersistenceStateSupplier
{
	PersistenceState getFor(ObservableObject object);
	
	boolean isTracked(ObservableObject object);
}
