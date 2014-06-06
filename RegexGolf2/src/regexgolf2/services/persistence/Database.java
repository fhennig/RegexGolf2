package regexgolf2.services.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
		Statement stmt;
		String sql;
		try
		{
			stmt = getConnection().createStatement();
			sql = 	"create table if not exists challenges" +
					"(id integer primary key autoincrement," +
					" solution integer not null references solution on delete no action on update cascade," +
					" name text not null); " +
					
					"create table if not exists solutions" +
					"(id integer primary key autoincrement," + 
					" challenge integer not null references challenge on delete cascade on update cascade," +
					" user integer," +
					"regex text not null); " +
					
					"create table if not exists requirements" +
					"(id integer primary key autoincrement," +
					" challenge integer not null references challenge on delete cascade on update cascade," +
					" expectedmatchresult boolean not null," +
					" word text not null); ";
			stmt.execute(sql);
			stmt.close();
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}
	
	public Connection getConnection()
	{
		return _connection;
	}
}
