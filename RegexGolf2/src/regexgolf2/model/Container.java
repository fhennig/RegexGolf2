package regexgolf2.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class Container<T> implements Iterable<T>
{
	private final List<ContainerChangedListener<? super T>> _listeners = new ArrayList<>();
	private final Set<T> _items = new HashSet<>();



	@Requires("item != null")
	@Ensures("contains(item)")
	public void add(T item)
	{
		boolean added = _items.add(item);
		if (added)
			fireContainerChangedEvent(new ContainerChangedEvent<T>(this, item, null));
	}
	
	@Ensures("!contains(item)")
	public void remove(T item)
	{
		boolean removed = _items.remove(item);
		if (removed)
			fireContainerChangedEvent(new ContainerChangedEvent<T>(this, null, item));
	}
	
	public boolean contains(T item)
	{
		return _items.contains(item);
	}

	@Override
	public Iterator<T> iterator()
	{
		final Iterator<T> iterator = _items.iterator();
		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return iterator.hasNext();
			}

			@Override
			public T next()
			{
				return iterator.next();
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	private void fireContainerChangedEvent(ContainerChangedEvent<T> event)
	{
		for (ContainerChangedListener<? super T> listener : _listeners)
			listener.containerChanged(event);
	}

	@Requires("listener != null")
	public void addListener(ContainerChangedListener<? super T> listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(ContainerChangedListener<? super T> listener)
	{
		_listeners.remove(listener);
	}
}
