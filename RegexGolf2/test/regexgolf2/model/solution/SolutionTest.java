package regexgolf2.model.solution;

import java.util.EventObject;

import org.junit.Assert;
import org.junit.Test;

public class SolutionTest
{
	private final Solution _solution = new Solution("");
	private boolean _solutionChanged = false;
	
	
	
	@Test
	public void testTrySetSolution()
	{
		Assert.assertTrue(_solution.trySetSolution(".*"));
		Assert.assertFalse(_solution.trySetSolution("[ab"));
		Assert.assertEquals(".*", _solution.getSolution());
	}
	
	@Test
	public void testSolutionChangedEvent()
	{
		_solution.addSolutionChangedListener(new SolutionChangedListener()
		{
			@Override
			public void solutionChanged(EventObject event)
			{
				_solutionChanged = true;
			}
		});
		
		//Test if invalid Syntax fires the event
		_solution.trySetSolution("[asdf");
		Assert.assertFalse(_solutionChanged);
		//Test if valid Syntax fires the event
		_solution.trySetSolution("Banana");
		Assert.assertTrue(_solutionChanged);
	}
	
	@Test
	public void testGetSolution()
	{
		_solution.trySetSolution("a*");
		Assert.assertEquals("a*", _solution.getSolution());
		_solution.trySetSolution("\\*");
		Assert.assertEquals("\\*", _solution.getSolution());
	}
}
