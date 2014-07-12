package regexgolf2.ui.challengegenerator;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import regexgolf2.model.challengegenerator.Generator;

public class GeneratorItem
{
	private final Generator _generator;
	
	
	
	@Requires("generator != null")
	public GeneratorItem(Generator generator)
	{
		_generator = generator;
	}
	
	
	
	@Ensures("result != null")
	public Generator getGenerator()
	{
		return _generator;
	}
	
	@Override
	public String toString()
	{
		return _generator.getName();
	}
}
