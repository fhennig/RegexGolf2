package regexgolf2.services.challengerepository;

import java.util.EventObject;

public interface TrackedObjectChangedListener
{
	void trackedObjectChanged(EventObject event);
}
