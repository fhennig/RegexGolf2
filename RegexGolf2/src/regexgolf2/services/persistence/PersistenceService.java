package regexgolf2.services.persistence;

import com.google.java.contract.Requires;

public class PersistenceService
{
	private final Database _db;
	
	
	
	@Requires("db != null")
	public PersistenceService(Database db)
	{
		_db = db;
	}
}
