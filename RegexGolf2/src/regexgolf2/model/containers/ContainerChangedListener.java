package regexgolf2.model.containers;

import java.util.EventListener;

import com.google.java.contract.Ensures;

public interface ContainerChangedListener<T> extends EventListener
{
	@Ensures("event != null")
	void containerChanged(ContainerChangedEvent<? extends T> event);
}
