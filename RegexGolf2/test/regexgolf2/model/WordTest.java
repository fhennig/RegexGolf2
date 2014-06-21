package regexgolf2.model;

import org.junit.Assert;
import org.junit.Test;

import com.google.java.contract.Ensures;

import regexgolf2.services.repositories.PersistenceState;
import regexgolf2.services.repositories.PersistenceStateImpl;
import regexgolf2.util.LogInitializer;
import regexgolf2.util.Validator;

public class WordTest
{
	private final Word _word;
	/**
	 * Used to test the events fired by the {@link Word Word} class.
	 */
	private final PersistenceState _pState;
	
	
	
	public WordTest()
	{
		LogInitializer.initializeLoggingSettings();
		_word = new Word();
		_word.trySetText("Test");
		
		_pState = new PersistenceStateImpl(_word, false);
	}
	
	
	
	@Test
	public void testGettersAndSetters()
	{
		_word.setId(42);
		Assert.assertEquals(42, _word.getId());
		
		_word.trySetText("New Text");
		Assert.assertEquals("New Text", _word.getText());
	}
	
	@Test
	public void testEventOnWordChanged()
	{
		Assert.assertFalse(_pState.isChanged());
		
		_word.trySetText("new Text");
		
		Assert.assertTrue(_pState.isChanged());
	}
	
	@Test
	public void testEventOnIdChanged()
	{
		Assert.assertFalse(_pState.isChanged());
		
		_word.setId(300);
		
		Assert.assertTrue(_pState.isChanged());
	}
	
	@Test
	public void testValidator()
	{
		_word.trySetText("start");
		//Sets the validator to accept only words with length >= 4
		_word.setTextValidator(getLengthValidator(4));
		
		boolean successful = _word.trySetText("abc"); //Too short
		Assert.assertFalse("Setting the text should not be successful, " + 
				"because the text is not valid.", successful);
		
		Assert.assertEquals("Text should not be changed, as the set should not happen, " + 
				"because the set text was not valid.", "start", _word.getText());
		
		successful = _word.trySetText("Game");
		
		Assert.assertTrue(successful);
		Assert.assertEquals("Game", _word.getText());
	}
	
	@Test
	public void testNullValidator()
	{
		_word.trySetText("start");
		_word.setTextValidator(getLengthValidator(10));
		
		_word.setTextValidator(null);
		boolean successful = _word.trySetText("abc");
		Assert.assertTrue(successful);
	}
	
	/**
	 * Returns a new Validator for Strings, that only accepts Strings
	 * with a length of at least 4.
	 */
	@Ensures("result != null")
	private static Validator<String> getLengthValidator(final int acceptedLength)
	{
		return new Validator<String>()
		{
			@Override
			public boolean isValid(String item)
			{
				if (item != null && item.length() >= acceptedLength)
					return true;
				return false;
			}
		};
	}
}
