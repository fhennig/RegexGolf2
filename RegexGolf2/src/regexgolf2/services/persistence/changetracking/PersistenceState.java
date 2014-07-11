package regexgolf2.services.persistence.changetracking;

import regexgolf2.model.ObjectChangedListener;

/**
 * Represents the state of an object in comparison to the version of it that is persisted. 
 */
public interface PersistenceState
{
	/**
	 * Indicates that the associated object is not persisted.
	 */
	boolean isNew();

	/**
	 * Indicates that the object version inside the application differs from the persisted version. 
	 */
	boolean isChanged();
	
	void addObjectChangedListener(ObjectChangedListener listener);
	void removeObjectChangedListener(ObjectChangedListener listener);
}
