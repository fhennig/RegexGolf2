package regexgolf2.model.regexgenerator;

import com.google.java.contract.Ensures;

import regexgolf2.model.Challenge;

public interface Generator
{
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
	Challenge generateChallenge();
	
	/**
	 * Should return a configuration object for this Generator.
	 */
	GeneratorConfig getConfig();
}
