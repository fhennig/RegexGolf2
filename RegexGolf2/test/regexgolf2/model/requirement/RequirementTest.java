package regexgolf2.model.requirement;

import org.junit.Assert;
import org.junit.Test;

import regexgolf2.model.solution.Solution;

public class RequirementTest
{
	private final Solution _houseSolution = new Solution("house");
	private final Solution _carSolution = new Solution("car");
	private final Requirement _houseRequirement = new Requirement(true, "house");
	
	@Test
	public void testIsCompliedPositive()
	{
		_houseRequirement.applySolution(_houseSolution);
		Assert.assertTrue(_houseRequirement.isComplied());	
	}
	
	@Test
	public void testIsCompliedNegative()
	{
		_houseRequirement.applySolution(_carSolution);
		Assert.assertFalse(_houseRequirement.isComplied());
	}
}
