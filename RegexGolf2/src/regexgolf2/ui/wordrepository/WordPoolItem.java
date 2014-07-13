package regexgolf2.ui.wordrepository;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import regexgolf2.model.containers.WordPool;

public class WordPoolItem
{
	private WordPool _pool;
	
	
	
	@Requires("pool != null")
	public WordPoolItem(WordPool pool)
	{
		_pool = pool;
	}
	
	
	
	@Ensures("result != null")
	public WordPool getWordPool()
	{
		return _pool;
	}
	
	@Override
	public String toString()
	{
		return _pool.getName();
	}
}
