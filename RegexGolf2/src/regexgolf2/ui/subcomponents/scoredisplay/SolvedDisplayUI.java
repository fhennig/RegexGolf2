package regexgolf2.ui.subcomponents.scoredisplay;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class SolvedDisplayUI
{
	private final Label _label;
	
	private final IntegerProperty _amountRequirements = new SimpleIntegerProperty();
	private final IntegerProperty _amountCompliedRequirements = new SimpleIntegerProperty();
	private final BooleanProperty _isHighlighted = new SimpleBooleanProperty();
	
		
	
	@Requires("label != null")
	public SolvedDisplayUI(Label label)
	{
		_label = label;
		initPropertyChangeReaction();
		refresh();
	}
	
	
	
	private void initPropertyChangeReaction()
	{
		ChangeListener<Object> cl = new ChangeListener<Object>()
		{
			@Override
			public void changed(ObservableValue<? extends Object> arg0,
					Object arg1, Object arg2)
			{
				refresh();
			}
		};
		
		_amountRequirements.addListener(cl);
		_amountCompliedRequirements.addListener(cl);
		_isHighlighted.addListener(cl);
	}
	
	public void setAmountRequirements(int amount)
	{
		_amountRequirements.set(amount);
	}
	
	@Ensures("result != null")
	public IntegerProperty amountRequirementsProperty()
	{
		return _amountRequirements;
	}
	
	public void setAmountCompliedRequirements(int amount)
	{
		_amountCompliedRequirements.set(amount);
	}
	
	@Ensures("result != null")
	public IntegerProperty amountCompliedRequirementsProperty()
	{
		return _amountCompliedRequirements;
	}
	
	public void setHighlighted(boolean highlighted)
	{
		_isHighlighted.set(highlighted);
	}
	
	@Ensures("result != null")
	public BooleanProperty isHighlightedProperty()
	{
		return _isHighlighted;
	}
	
	public void refresh()
	{		
		String displayValue = _amountCompliedRequirements.get() + "/" + _amountRequirements.get();
		_label.setText(displayValue);

		if (_isHighlighted.get())
			_label.setTextFill(Color.GREEN); //check-color: 5fd251
		else
			_label.setTextFill(Color.BLACK);
	}
}
