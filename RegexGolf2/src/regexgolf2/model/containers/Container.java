package regexgolf2.model.containers;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.java.contract.Requires;

/**
 * A collection that is observable.
 * An Event is fired if an Item is added or removed.
 *
 * @param <T> The Type of the Elements
 */
public class Container<T> extends AbstractCollection<T>
{
	private final List<ContainerChangedListener<? super T>> _listeners = new ArrayList<>();
	private final Set<T> _items = new HashSet<>();



	@Requires("item != null")
	@Override
	public boolean add(T item)
	{
		boolean added = _items.add(item);
		if (added)
			fireContainerChangedEvent(new ContainerChangedEvent<T>(this, item, null));
		return added;
	}

	@Override
	public int size()
	{
		return _items.size();
	}

	@Override
	public Iterator<T> iterator()
	{
		final Iterator<T> iterator = _items.iterator();
		return new Iterator<T>()
		{
			private T _currentItem;
			
			@Override
			public boolean hasNext()
			{
				return iterator.hasNext();
			}

			@Override
			public T next()
			{
				_currentItem = iterator.next();
				return _currentItem;
			}

			@Override
			public void remove()
			{
				iterator.remove();
				fireContainerChangedEvent(new ContainerChangedEvent<>(Container.this, null, _currentItem));
			}
		};
	}

	private final void fireContainerChangedEvent(ContainerChangedEvent<T> event)
	{
		for (ContainerChangedListener<? super T> listener : _listeners)
			listener.containerChanged(event);
	}

	@Requires("listener != null")
	public final void addListener(ContainerChangedListener<? super T> listener)
	{
		_listeners.add(listener);
	}

	public final void removeListener(ContainerChangedListener<? super T> listener)
	{
		_listeners.remove(listener);
	}
}
