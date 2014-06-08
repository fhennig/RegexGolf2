package regexgolf2.startup;

import regexgolf2.model.Challenge;
import regexgolf2.model.Requirement;

public class ChallengeFactory
{
	public static Challenge getTestChallenge()
	{
		Challenge challenge = new Challenge();
		
		challenge.setName("Test-Challenge 1");
		
		challenge.addRequirement(new Requirement(true, "Haus"));
		challenge.addRequirement(new Requirement(true, "Baum"));
		challenge.addRequirement(new Requirement(false, "Auto"));
		challenge.addRequirement(new Requirement(false, "Tier"));
		
		return challenge;
	}
	
	public static Challenge getIPChallenge()
	{
		Challenge challenge = new Challenge();
		
		challenge.setName("IP-Challenge");
		
		challenge.addRequirement(new Requirement(true, "1.2.3.4"));
		challenge.addRequirement(new Requirement(true, "192.168.1.1"));
		challenge.addRequirement(new Requirement(true, "81.80.79.78"));
		challenge.addRequirement(new Requirement(false, "300.0.0.0"));
		challenge.addRequirement(new Requirement(false, "256.0.0.256"));
		challenge.addRequirement(new Requirement(false, "12.13.14.15.16"));
		challenge.addRequirement(new Requirement(false, "12.13.14.1545"));
		
		return challenge;
	}
	
	public static Challenge getTestChallenge2()
	{
		Challenge challenge = new Challenge();
		
		challenge.setName("Test-Challenge 2");
		
		challenge.addRequirement(new Requirement(true, "Fenster"));
		challenge.addRequirement(new Requirement(true, "Dach"));
		challenge.addRequirement(new Requirement(false, "Tisch"));
		challenge.addRequirement(new Requirement(false, "Giesskanne"));
		
		return challenge;
	}
}
