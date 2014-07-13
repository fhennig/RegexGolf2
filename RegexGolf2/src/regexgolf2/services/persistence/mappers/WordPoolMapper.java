package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.Word;
import regexgolf2.model.containers.WordPool;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.database.Database;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class WordPoolMapper
{
	private final Database _db;
	private final WordMapper _words;
	
	
	
	@Requires({
		"db != null",
		"wm != null"
	})
	public WordPoolMapper(Database db, WordMapper wm)
	{
		_db = db;
		_words = wm;
	}
	
	

	@Ensures("result != null")
	public List<WordPool> getAll() throws PersistenceException
	{
		try
		{
			List<WordPool> result = new ArrayList<>();
			
			String sql = "SELECT id, name FROM wordpools; ";
			
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				WordPool pool = new WordPool();
				pool.setId(rs.getInt(1));
				pool.setName(rs.getString(2));
				
				result.add(pool);
			}
			ps.close();

			for (WordPool p : result)
			{
				List<Word> words = _words.getAll(p.getId());
				p.addAll(words);
			}
			
			return result;
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}

	@Requires("pool != null")
	public void insert(WordPool pool) throws PersistenceException
	{
		int id = getNextPoolId();
		
		String sql = "INSERT INTO wordpools (id, name) VALUES (?, ?); ";
		
		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setInt(1, id);
			ps.setString(2, pool.getName());
			ps.execute();
			ps.close();
			
			for (Word w : pool)
				_words.insert(w, id);
			
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
		
		pool.setId(id);
	}
	
	private int getNextPoolId() throws PersistenceException
	{
		String sql = "SELECT CASE WHEN count(*) = 0 THEN 1 ELSE max(id) + 1 END FROM wordpools; ";

		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int nextId = rs.getInt(1);
			ps.close();
			return nextId;
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}
	
	@Requires("pool != null")
	public void update(WordPool pool) throws PersistenceException
	{
		try
		{
			String sql = "UPDATE wordpools SET name = ? WHERE id = ?; "; 
			
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setString(1, pool.getName());
			ps.setInt(2, pool.getId());
			ps.execute();
			ps.close();

			for(Word w : pool)
				_words.update(w, pool.getId());
			
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}
	
	public void delete(WordPool pool) throws PersistenceException
	{
		try
		{
			String sql = "DELETE FROM wordpools WHERE id = ?; ";
			
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setInt(1, pool.getId());
			ps.execute();
			ps.close();
			
			//Words deleted via cascade
		} catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}
}
