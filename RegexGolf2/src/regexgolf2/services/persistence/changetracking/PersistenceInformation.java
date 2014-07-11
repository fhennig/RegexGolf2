package regexgolf2.services.persistence.changetracking;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import regexgolf2.model.ObservableObject;

public interface PersistenceInformation
{
	/**
	 * Returns a PersistenceState object, that updates itself.
	 */
	@Requires("isTracked(object)")
	@Ensures("result != null")
	PersistenceState getPersistenceState(ObservableObject object);

	/**
	 * Method to conveniently access the 'isNew' information for a tracked
	 * object.
	 */
	@Requires("isTracked(object)")
	boolean isNew(ObservableObject object);


	/**
	 * Method to conveniently access the 'isChanged' information for a tracked
	 * object.
	 */
	@Requires("isTracked(object)")
	boolean isChanged(ObservableObject object);

	/**
	 * Indicates if there is any persistence information about the given object
	 * available.
	 */
	@Requires("object != null")
	boolean isTracked(ObservableObject object);
}
