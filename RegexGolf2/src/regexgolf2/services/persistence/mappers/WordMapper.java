package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.Word;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.database.Database;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class WordMapper
{
	private final Database _db;



	@Requires("db != null")
	public WordMapper(Database db)
	{
		_db = db;
	}



	@Ensures("result != null")
	public List<Word> getAll(int poolId) throws PersistenceException
	{
		try
		{
			List<Word> result = new ArrayList<>();

			String sql = "SELECT id, text FROM words WHERE pool_id = ?; ";

			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setInt(1, poolId);
			ResultSet rs = ps.executeQuery();

			while (rs.next())
			{
				Word word = new Word();
				word.setId(rs.getInt(1));
				word.trySetText(rs.getString(2));

				result.add(word);
			}
			ps.close();

			return result;
		} catch (SQLException e)
		{
			throw new PersistenceException();
		}
	}

	@Requires("word != null")
	public void insert(Word word, int poolId) throws PersistenceException
	{
		int id = getNextWordId(poolId); // Cache id in field first, only set to word
									// if insert was successful

		String sql = "INSERT INTO words (pool_id, id, text) VALUES (?, ?, ?); ";
		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setInt(1, poolId);
			ps.setInt(2, id);
			ps.setString(3, word.getText());
			ps.execute();
			ps.close();
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}

		// everything was successful; set ID in word-object
		word.setId(id);
	}

	private int getNextWordId(int poolId) throws PersistenceException
	{
		String sql = "SELECT CASE WHEN count(*) = 0 THEN 1 ELSE max(id) + 1 END FROM "
				+ "(select * from words where pool_id = ?); ";

		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setInt(1, poolId);
			ResultSet rs = ps.executeQuery();
			int nextId = rs.getInt(1);
			ps.close();
			return nextId;
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}

	@Requires("word != null")
	public void update(Word word, int poolId) throws PersistenceException
	{
		String sql = "UPDATE words SET text = ? WHERE pool_id = ? AND id = ?; ";

		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setString(1, word.getText());
			ps.setInt(2, poolId);
			ps.setInt(3, word.getId());
			ps.execute();
			ps.close();
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}

	@Requires("word != null")
	public void delete(Word word, int poolId) throws PersistenceException
	{
		String sql = "DELETE FROM words WHERE pool_id = ? AND id = ?; ";

		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setInt(1, poolId);
			ps.setInt(2, word.getId());
			ps.execute();
			ps.close();
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}
}
