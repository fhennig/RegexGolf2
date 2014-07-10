package regexgolf2.services.persistence.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class Database
{
	private Connection _connection;
	
	
	
	@Requires({
		"file != null",
		"file.exists()"
	})
	public Database(File file) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		_connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
		enableForeignKeySupport();
	}
	
	
	
	private void enableForeignKeySupport() throws SQLException
	{
		String enableForeignKeySupportSQL = "pragma foreign_keys = on;";

		PreparedStatement stmt = getConnection().prepareStatement(enableForeignKeySupportSQL);
		stmt.execute();
		stmt.close();
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
