package regexgolf2.services;

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
