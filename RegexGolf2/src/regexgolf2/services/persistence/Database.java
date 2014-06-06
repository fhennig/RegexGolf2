package regexgolf2.services.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.java.contract.Ensures;
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
		createTablesIfNotExist();
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
			throw new DatabaseException(ex);
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalStateException("SQLite JDBC driver not found!");
		}
	}
	
	private void createTablesIfNotExist()
	{
		String createChallengesTableSQL = "create table if not exists challenges" +
				"(id integer primary key autoincrement," +
				" regex text not null," +
				" name text not null); ";
		
		String createSolutionsTableSQL = "create table if not exists solutions" +
				"(id integer primary key autoincrement," + 
				" challenge integer not null references challenge on delete cascade on update cascade," +
				" regex text not null); ";
		
		String createRequirementsTableSQL = "create table if not exists requirements" +
				"(id integer primary key autoincrement," + 
				" challenge integer not null references challenge on delete cascade on update cascade," +
				" expectedmatchresult boolean, " +
				"word text not null); "; 
		
		Statement stmt;
		try
		{
			stmt = getConnection().createStatement();
			stmt.execute(createChallengesTableSQL);
			stmt.execute(createSolutionsTableSQL);
			stmt.execute(createRequirementsTableSQL);
			stmt.close();
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}
	
	/**
	 * Connection should not be closed!
	 */
	@Ensures("result != null")
	public Connection getConnection()
	{
		return _connection;
	}
}
