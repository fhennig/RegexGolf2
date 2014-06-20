package regexgolf2.services.repositories;

import regexgolf2.model.ObjectChangedListener;


public interface PersistenceState
{
	boolean isNew();
	
	//TODO write requires (if isnew ==> isChanged)
	boolean isChanged();
	
	Object getObservedItem();
	
	void addObjectChangedListener(ObjectChangedListener listener);
	void removeObjectChangedListener(ObjectChangedListener listener);
}
