package regexgolf2.ui;

import regexgolf2.model.solution.Solution;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class SolutionUI
{
	private final TextField _textField;
	private Solution _solution;
	private boolean _isInit;
	
	
	
	public SolutionUI(TextField textField)
	{
		_textField = textField;
		init();
	}
	
	
	
	private void init()
	{
		_textField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2)
			{
				if (!_isInit && _solution != null)
				{
					_solution.trySetSolution(_textField.getText());
				}
			}
		});
	}
	
	public void setSolution(Solution solution)
	{
		_solution = solution;
		_isInit = true;
		_textField.setText(_solution.getSolution());
		_isInit = false;
	}
}
