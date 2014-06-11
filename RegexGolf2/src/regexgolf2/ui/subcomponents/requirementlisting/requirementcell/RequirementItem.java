package regexgolf2.ui.subcomponents.requirementlisting.requirementcell;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import regexgolf2.model.Requirement;

public class RequirementItem
{
	private final StringProperty _word = new SimpleStringProperty();
	private final BooleanProperty _complied = new SimpleBooleanProperty();
	private final Requirement _requirement;
		


	@Requires("requirement != null")
	public RequirementItem(Requirement requirement, boolean isComplied)
	{
		_requirement = requirement;
		setWord(_requirement.getWord());
		setComplied(isComplied);
	}
	
	
	
	@Ensures("result != null")
	public Requirement getRequirement()
	{
		return _requirement;
	}
	
	@Requires("word != null")
	public void setWord(String word)
	{
		_word.set(word);
	}
	
	@Ensures("result != null")
	public StringProperty wordProperty()
	{
		return _word;
	}
	
	public void setComplied(boolean complied)
	{
		_complied.set(complied);
	}
	
	@Ensures("result != null")
	public BooleanProperty compliedProperty()
	{
		return _complied;
	}
}
