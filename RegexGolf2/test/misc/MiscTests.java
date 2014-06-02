package misc;

import org.junit.Assert;
import org.junit.Test;

public class MiscTests
{
	@Test
	public void testBooleanWrapperClass()
	{
		Boolean bObj = new Boolean(true);
		
		Assert.assertTrue(bObj == true);
	}
}
