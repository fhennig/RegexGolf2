package regexgolf2.services.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.java.contract.Requires;

public class Database
{
	private Connection _connection;
	
	
	
	@Requires({
		"file != null",
		"file.exists()"
	})
	public Database(File file)
	{
		init(file.getPath());
	}
	
	
	
	private void init(String path)
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			_connection = DriverManager.getConnection("jdbc:sqlite:" + path);
		}
		catch (SQLException ex)
		{
			//TODO exception handling
			System.out.println(ex);
		}
		catch (ClassNotFoundException e)
		{
			// throw as RuntimeEx
			System.out.println(e);
		}
	}
	
	public Connection getConnection()
	{
		return _connection;
	}
}
