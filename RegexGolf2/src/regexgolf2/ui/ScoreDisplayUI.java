package regexgolf2.ui;

import com.google.java.contract.Requires;

import javafx.scene.control.Label;

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
	
	private void refreshUI()
	{		
		String displayValue = _amountCompliedRequirements + "/" + _amountRequirements;
		_label.setText(displayValue);
	}
}
