package regexgolf2.model.regexgenerator;

import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.Word;

import com.google.java.contract.Ensures;

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
	Challenge generateChallenge(List<Word> words);
	
	/**
	 * Should return a configuration object for this Generator.
	 */
	GeneratorConfig getConfig();
}
