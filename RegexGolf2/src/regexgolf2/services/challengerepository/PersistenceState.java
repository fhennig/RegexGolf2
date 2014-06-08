package regexgolf2.services.challengerepository;

import regexgolf2.model.ObjectChangedListener;


public interface PersistenceState
{
	boolean isNew();
	
	//TODO write requires (if isnew ==> isChanged)
	boolean isChanged();
	
	void addObjectChangedListener(ObjectChangedListener listener);
	void removeObjectChangedListener(ObjectChangedListener listener);
}
