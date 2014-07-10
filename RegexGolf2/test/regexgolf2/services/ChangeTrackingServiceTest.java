package regexgolf2.services;

import static org.junit.Assert.*;

import org.junit.Test;

import regexgolf2.model.ObservableObject;

public class ChangeTrackingServiceTest
{
	private static class Item extends ObservableObject
	{
	}
	
	
	
	private ChangeTrackingService _cts;
	
	
	
	public ChangeTrackingServiceTest()
	{
		_cts = new ChangeTrackingService();
	}
	
	
	
	@Test
	public void testTrackUntrack()
	{
		Item item = new Item();
		assertFalse(_cts.isTracked(item));
		_cts.track(item);
		assertTrue(_cts.isTracked(item));
		assertNotEquals(null, _cts.getPersistenceState(item));
		_cts.untrack(item);
		assertFalse(_cts.isTracked(item));
	}
}
