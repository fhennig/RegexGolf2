package regexgolf2.startup;

import java.util.Random;

import com.google.java.contract.Ensures;

import regexgolf2.model.Challenge;
import regexgolf2.model.Requirement;

public class ChallengeFactory
{
	@Ensures("result != null")
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

	@Ensures("result != null")
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

	@Ensures("result != null")
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

	@Ensures("result != null")
	public static Challenge getRandomChallenge()
	{
		int random = new Random().nextInt();
		random = Math.abs(random);
		return getChallenge(random);
	}
	
	@Ensures("result != null")
	public static Challenge getChallenge(int number)
	{
		Challenge result;
		
		switch(number % 3)
		{
		case  0 : result = getTestChallenge(); break;
		case  1 : result = getIPChallenge(); break;
		case  2 : result = getTestChallenge2(); break;
		default : result = new Challenge(); break;
		}
		return result;
	}
}
