package regexgolf2.services.persistence;

/**
 * This class is thrown if an error with persistance occurs.
 * For example, if an SQLException occurs, it should be wrapped in this class. 
 */
@SuppressWarnings("serial")
public class PersistenceException extends Exception
{
	public PersistenceException()
	{
		super();
	}

	public PersistenceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PersistenceException(String message)
	{
		super(message);
	}

	public PersistenceException(Throwable cause)
	{
		super(cause);
	}
}
