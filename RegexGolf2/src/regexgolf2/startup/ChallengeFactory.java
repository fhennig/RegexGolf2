package regexgolf2.startup;

import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.model.solution.Solution;

public class ChallengeFactory
{
	public static Challenge getTestChallenge()
	{
		Challenge challenge = new Challenge();
		
		challenge.addRequirement(new Requirement(true, "Haus"));
		challenge.addRequirement(new Requirement(true, "Baum"));
		challenge.addRequirement(new Requirement(false, "Auto"));
		challenge.addRequirement(new Requirement(false, "Tier"));
		
		return challenge;
	}
	
	public static Challenge getIPChallenge()
	{
		Challenge challenge = new Challenge();
		
		challenge.addRequirement(new Requirement(true, "1.2.3.4"));
		challenge.addRequirement(new Requirement(true, "192.168.1.1"));
		challenge.addRequirement(new Requirement(true, "81.80.79.78"));
		challenge.addRequirement(new Requirement(false, "300.0.0.0"));
		challenge.addRequirement(new Requirement(false, "256.0.0.256"));
		challenge.addRequirement(new Requirement(false, "12.13.14.15.16"));
		challenge.addRequirement(new Requirement(false, "12.13.14.1545"));
		
		return challenge;
	}
}
