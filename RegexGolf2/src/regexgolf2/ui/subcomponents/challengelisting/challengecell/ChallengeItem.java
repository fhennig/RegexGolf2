package regexgolf2.ui.subcomponents.challengelisting.challengecell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeItem
{
	private final StringProperty _name = new SimpleStringProperty();
	private final IntegerProperty _amountRequirements = new SimpleIntegerProperty();
	private final IntegerProperty _amountCompliedRequirements = new SimpleIntegerProperty();
	private final BooleanProperty _isChanged = new SimpleBooleanProperty();
	
	
	
	@Requires("name != null")
	public ChallengeItem(String name, int amountRequirements, int amountComplied, boolean isChanged)
	{
		update(name, amountRequirements, amountComplied, isChanged);
	}
	
	
	
	/**
	 * Updates the values in this ChallengeItem
	 * @param name  The Name of the Challenge
	 * @param aR  Amount of Requirements in the Challenge
	 * @param aC  Amount of complied Requirements in Challenge
	 */
	@Requires("name != null")
	public void update(String name, int aR, int aC, boolean isChanged)
	{
		_name.set(name);
		_amountRequirements.set(aR);
		_amountCompliedRequirements.set(aC);
		_isChanged.set(isChanged);
	}
	
	@Ensures("result != null")
	public StringProperty nameProperty()
	{
		return _name;
	}
	
	@Ensures("result != null")
	public IntegerProperty amountRequirementsProperty()
	{
		return _amountRequirements;
	}
	
	@Ensures("result != null")
	public IntegerProperty amountCompliedRequirementsProperty()
	{
		return _amountCompliedRequirements;
	}
	
	@Ensures("result != null")
	public BooleanProperty isChangedProperty()
	{
		return _isChanged;
	}
}
