package regexgolf2.services.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import com.google.java.contract.Requires;

public class DatabaseInitializer
{
	private final Database _db;
	
	
	
	@Requires("db != null")
	public DatabaseInitializer(Database db)
	{
		_db = db;
	}
	
	
	
	public void createTablesIfNotExists() throws SQLException
	{
		String createChallengesTableSQL = "create table if not exists challenges (" +
				"id integer primary key autoincrement," +
				"regex text not null," +
				"name text not null); ";
		
		String createSolutionsTableSQL = "create table if not exists solutions (" +
				"id integer primary key autoincrement," + 
				"challenge integer not null references challenges(id) on delete cascade on update cascade," +
				"regex text not null); ";
		
		String createRequirementsTableSQL = "create table if not exists requirements (" +
				"id integer primary key autoincrement," + 
				"challenge integer not null references challenges(id) on delete cascade on update cascade," +
				"expectedmatchresult boolean, " +
				"word text not null); "; 
		
		String createWordTableSQL = "create table if not exists words (" +
				"id integer primary key autoincrement," +
				"text text not null unique); ";
		
		String enableForeignKeySupportSQL = "pragma foreign_keys = on;";
		
		Statement stmt;
		stmt = _db.getConnection().createStatement();
		stmt.execute(createChallengesTableSQL);
		stmt.execute(createSolutionsTableSQL);
		stmt.execute(createRequirementsTableSQL);
		stmt.execute(createWordTableSQL);
		stmt.execute(enableForeignKeySupportSQL);
		stmt.close();
	}
	
	public void insertDefaultWords() throws SQLException
	{
		List<String> words = Arrays.asList("Haus", "Baum", "Auto", "Dach");
		
		String sql = "INSERT OR IGNORE INTO words (text) VALUES (?); ";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		
		for (String word : words)
		{
			ps.setString(1, word);
			ps.execute();
		}
		ps.close();
	}
}
