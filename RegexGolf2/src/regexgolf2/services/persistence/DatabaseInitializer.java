package regexgolf2.services.persistence;

import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer
{
	public void createTablesIfNotExists(Database db) throws SQLException
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
		
		String enableForeignKeySupportSQL = "pragma foreign_keys = on;";
		
		Statement stmt;
		stmt = db.getConnection().createStatement();
		stmt.execute(createChallengesTableSQL);
		stmt.execute(createSolutionsTableSQL);
		stmt.execute(createRequirementsTableSQL);
		stmt.execute(enableForeignKeySupportSQL);
		stmt.close();
	}
}
