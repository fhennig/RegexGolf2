package regexgolf2.model.containers;

public class WordPoolContainer extends Container<WordPool>
{
	public WordPool createNew()
	{
		WordPool pool = new WordPool();
		add(pool);
		return pool;
	}
}
