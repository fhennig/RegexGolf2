package regexgolf2.model.challengegenerator;

import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.Word;

import com.google.java.contract.Ensures;

public interface Generator
{
	static GeneratorConfig EMPTY_CONFIG = new EmptyConfig();
	
	/**
	 * This method should return a displayable String that
	 * describes and identifies the Generator.
	 * The Name of a Generator should not change.
	 */
	@Ensures("result != null")
	String getName();
	
	/**
	 * This method should generate a new Challenge and return it.
	 */
	@Ensures("result != null")
	Challenge generateChallenge(List<Word> words);
	
	/**
	 * Should return a configuration object for this Generator.
	 * If the Generator is not configurable,
	 * {@link #EMPTY_CONFIG} is returned.
	 */
	@Ensures("result != null")
	default GeneratorConfig getConfig()
	{
		return EMPTY_CONFIG;
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
