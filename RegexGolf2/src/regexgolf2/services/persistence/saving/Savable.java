package regexgolf2.services.persistence.saving;

import regexgolf2.services.persistence.PersistenceException;

public interface Savable
{
	void accept(SaveVisitor visitor) throws PersistenceException;
}
