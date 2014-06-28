package regexgolf2.services.challengegenerator;

import java.util.HashSet;
import java.util.Set;

import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.regexgenerator.Generator;
import regexgolf2.model.regexgenerator.TestGenerator;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeGeneratorService
{
	private Set<Generator> _generators = new HashSet<>();
	private Generator _selectedGenerator;
	
	
	
	public ChallengeGeneratorService()
	{
		initGenerators();
	}
	
	
	
	private void initGenerators()
	{
		Generator g = new TestGenerator();
		_generators.add(g);
		_selectedGenerator = g;
	}
	
	@Requires("_generators.contains(generator)")
	public void setSelectedGenerator(Generator generator)
	{
		_selectedGenerator = generator;
	}
	
	@Ensures("result != null")
	public Generator getSelectedGenerator()
	{
		return _selectedGenerator;
	}
	
	public SolvableChallenge generateChallenge()
	{
		Challenge c = getSelectedGenerator().generateChallenge();
		SolvableChallenge sc = new SolvableChallenge(new Solution(), c);
		
		return sc;
	}
}
