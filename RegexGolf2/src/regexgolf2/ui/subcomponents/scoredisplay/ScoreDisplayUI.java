package regexgolf2.ui.subcomponents.scoredisplay;

import com.google.java.contract.Requires;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ScoreDisplayUI
{
	private final Label _label;
	private int _amountRequirements;
	private int _amountCompliedRequirements;
	
		
	
	@Requires("label != null")
	public ScoreDisplayUI(Label label)
	{
		_label = label;
		refreshUI();
	}
	
	public void setAmountRequirements(int amount)
	{
		_amountRequirements = amount;
		refreshUI();
	}
	
	public void setAmountCompliedRequirements(int amount)
	{
		_amountCompliedRequirements = amount;
		refreshUI();
	}
	
	public void setHighlight(boolean highlight)
	{
		if (highlight)
			_label.setTextFill(Color.GREEN); //check-color: 5fd251
		else
			_label.setTextFill(Color.BLACK);
	}
	
	private void refreshUI()
	{		
		String displayValue = _amountCompliedRequirements + "/" + _amountRequirements;
		_label.setText(displayValue);
	}
}
