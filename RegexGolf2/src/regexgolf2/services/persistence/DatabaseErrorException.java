package regexgolf2.services.persistence;

public class DatabaseErrorException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DatabaseErrorException(Throwable cause)
	{
		super(cause);
	}
}
