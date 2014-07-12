package regexgolf2.services.persistence;

import javax.swing.JOptionPane;

import regexgolf2.model.ObservableObject;
import regexgolf2.model.containers.ContainerChangedEvent;
import regexgolf2.model.containers.ContainerChangedListener;
import regexgolf2.services.persistence.changetracking.PersistenceInformation;

import com.google.java.contract.Requires;

/**
 * An instance of this class can be attached to a {@link Container}.
 * It will delete any Item from the database that is removed from the container.
 */
public class DeleteHandler<T extends ObservableObject> implements ContainerChangedListener<T>
{
	/**
	 * The actual strategy to delete an item from the database
	 */
	public static interface DeleteStrategy<T>
	{
		void delete(T item) throws PersistenceException;
	}



	private final PersistenceInformation _pss;
	private final DeleteStrategy<T> _deleteStrategy;



	@Requires(
	{ "pss != null", "deleteStrategy != null" })
	public DeleteHandler(PersistenceInformation pss, DeleteStrategy<T> deleteStrategy)
	{
		_pss = pss;
		_deleteStrategy = deleteStrategy;
	}



	@Override
	public void containerChanged(ContainerChangedEvent<? extends T> event)
	{
		T item = event.getRemovedItem();
		if (item == null)
			return;
		if (_pss.isTracked(item))
		{
			boolean needsDBDelete = !_pss.isNew(item);
			if (needsDBDelete)
				performDBDelete(item);
		}
	}

	private void performDBDelete(T item)
	{
		try
		{
			_deleteStrategy.delete(item);
		} catch (PersistenceException e)
		{
			//TODO use proper error handler
			JOptionPane.showMessageDialog(null, "DB Error while deleting");
		}
	}
}
