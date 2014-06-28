package regexgolf2.model.regexgenerator;

import com.google.java.contract.Requires;

/**
 * The AbstractGenerator class implements the Generator interface and ensures
 * some of the requirements imposed by the interface.
 * It can be used for implementational inheritance.
 */
abstract class AbstractGenerator implements Generator
{
	private final String _name;
	private static final GeneratorConfig _EMPTY_CONFIG = new EmptyConfig();
	private GeneratorConfig _config;
	
	
	
	@Requires("name != null")
	public AbstractGenerator(String name)
	{
		_name = name;
		_config = _EMPTY_CONFIG;
	}
	
	
	
	@Override
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * Sets the configuration object used by the generator.
	 * Null is a valid argument in this case, this class
	 * reacts to the null argument and ensures that 
	 * {@link #getConfig()} will never return null. 
	 */
	protected void setConfig(GeneratorConfig config)
	{
		if (config == null)
			_config = _EMPTY_CONFIG;
		else
			_config = config;
	}
	
	@Override
	public final GeneratorConfig getConfig()
	{
		return _config;
	}
	
	
	
	/**
	 * An empty configuration class, that serves as a default
	 * "null-object".
	 */
	public static class EmptyConfig implements GeneratorConfig
	{
		@Override
		public void accept(GeneratorConfigVisitor visitor)
		{
			visitor.visit(this);
		}
	}
}
