package regexgolf2.services.persistence;

public class DatabaseException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DatabaseException(Throwable cause)
	{
		super(cause);
	}
}
