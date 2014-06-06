package regexgolf2.services.persistence;

import java.util.List;

import regexgolf2.model.Challenge;

public interface PersistenceServiceOld
{
	List<Challenge> getAll();
	
	public void insert(Challenge challenge);
	
	public void update(Challenge challenge);
	
	public void delete(int id);
}
