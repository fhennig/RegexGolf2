package regexgolf2.services;

import javax.swing.JOptionPane;

import regexgolf2.model.ContainerChangedEvent;
import regexgolf2.model.ContainerChangedListener;
import regexgolf2.model.ObservableObject;

import com.google.java.contract.Requires;

public class DeleteHandler<T extends ObservableObject> implements ContainerChangedListener<T>
{
	public static interface DeleteStrategy<T>
	{
		void delete(T item) throws PersistenceException;
	}



	private final ChangeTrackingService _cts;
	private final DeleteStrategy<T> _deleteStrategy;



	@Requires(
	{ "cts != null", "deleteStrategy != null" })
	public DeleteHandler(ChangeTrackingService cts, DeleteStrategy<T> deleteStrategy)
	{
		_cts = cts;
		_deleteStrategy = deleteStrategy;
	}



	@Override
	public void containerChanged(ContainerChangedEvent<? extends T> event)
	{
		T item = event.getRemovedItem();
		if (item == null)
			return;
		if (_cts.isTracked(item))
		{
			boolean needsDBDelete = !_cts.getPersistenceState(item).isNew();
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
			JOptionPane.showMessageDialog(null, "DB Error while deleting");
		}
	}
}
