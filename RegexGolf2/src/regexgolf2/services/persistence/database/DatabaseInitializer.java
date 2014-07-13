package regexgolf2.services.persistence.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import regexgolf2.model.Word;
import regexgolf2.model.containers.WordPool;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.mappers.Mappers;

import com.google.java.contract.Requires;

public class DatabaseInitializer
{
	private final Database _db;



	@Requires("db != null")
	public DatabaseInitializer(Database db)
	{
		_db = db;
	}



	/**
	 * Creates all the Tables that are needed for the Application to run
	 * properly. However, if a Table already exists, even if it does not have
	 * the correct columns, it is not replaced.
	 */
	public void createTablesIfNotExists() throws SQLException
	{
		String createChallengesTableSQL = "create table if not exists challenges ("
				+ "id integer primary key autoincrement," + "regex text not null,"
				+ "name text not null); ";

		String createSolutionsTableSQL = "create table if not exists solutions ("
				+ "id integer primary key autoincrement,"
				+ "challenge integer not null references challenges(id) on delete cascade on update cascade,"
				+ "regex text not null); ";

		String createRequirementsTableSQL = "create table if not exists requirements ("
				+ "id integer primary key autoincrement,"
				+ "challenge integer not null references challenges(id) on delete cascade on update cascade,"
				+ "expectedmatchresult boolean, " + "word text not null); ";

		String createWordPoolTableSQL = "create table if not exists wordpools ("
				+ "id integer primary key autoincrement,"
				+ "name text not null); ";
		
		String createWordTableSQL = "create table if not exists words ("
				+ "pool_id integer not null references wordpools(id) on delete cascade on update cascade,"
				+ "id integer not null,"
				+ "text text not null,"
				+ "primary key(pool_id, id)); ";
		
		String createUniqueIndexPoolIdTextSQL = "create unique index words_unique_poolId_word on "
				+ "words (pool_id ASC, text ASC); ";

		Statement stmt;
		stmt = _db.getConnection().createStatement();
		stmt.execute(createChallengesTableSQL);
		stmt.execute(createSolutionsTableSQL);
		stmt.execute(createRequirementsTableSQL);
		stmt.execute(createWordPoolTableSQL);
		stmt.execute(createWordTableSQL);
		stmt.execute(createUniqueIndexPoolIdTextSQL);
		stmt.close();
	}

	/**
	 * Inserts some default Words into the Database which are used by the
	 * Challenge Generator.
	 * @throws PersistenceException 
	 */
	public void insertDefaultWords(Mappers mappers) throws PersistenceException
	{
		WordPool pool1 = new WordPool();
		pool1.setName("Pool 1");
		
		List<String> words = Arrays.asList("Haus", "Baum", "Auto", "Dach", "Gartenzaun",
				"Kamikaze", "Fenster", "Test", "Waschmaschine", "Fahrrad", "Hallo Welt!",
				"schwindelig", "Kollektiv Turmstrasse");

		words.forEach(string -> pool1.add(new Word(string)));
		
		mappers.getWordPoolMapper().insert(pool1);
		
		WordPool pool2 = new WordPool();
		pool2.setName("Pool 2");
		
		List<String> words2 = Arrays.asList("A", "B", "Arm", "Dax", "Garten",
				"Katze", "Fisch", "Tisch", "Maschine", "Rad", "Hallo");

		words2.forEach(string -> pool2.add(new Word(string)));
		
		mappers.getWordPoolMapper().insert(pool2);
	}
}
