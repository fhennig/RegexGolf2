package regexgolf2.services;

import com.google.java.contract.Requires;

import regexgolf2.model.ContainerChangedEvent;
import regexgolf2.model.ContainerChangedListener;
import regexgolf2.model.ObservableObject;

public class TrackHandler implements ContainerChangedListener<ObservableObject>
{
	private final ChangeTrackingService _cts;
	
	
	
	@Requires("changeTrackingService != null")
	public TrackHandler(ChangeTrackingService changeTrackingService)
	{
		_cts = changeTrackingService;
	}
	
	
	
	@Override
	public void containerChanged(ContainerChangedEvent<? extends ObservableObject> event)
	{
		if (event.getAddedItem() != null)
			_cts.track(event.getAddedItem());
		if (event.getRemovedItem() != null)
			_cts.untrack(event.getRemovedItem());
		//TODO what happens when the added item is already tracked?
		//this should be prevented
	}

}
