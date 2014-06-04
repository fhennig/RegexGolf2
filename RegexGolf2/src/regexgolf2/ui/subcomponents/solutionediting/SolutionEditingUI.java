package regexgolf2.ui.subcomponents.solutionediting;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class SolutionEditingUI
{
	private final List<TextChangedListener> _listeners = new ArrayList<>();
	private final TextField _textField;
	private boolean _isInternalTextChange;
	
	
	
	public SolutionEditingUI(TextField textField)
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
				if (!_isInternalTextChange)
				{
					fireTextChangedEvent();
				}
			}
		});
	}

	public void setText(String text)
	{
		_isInternalTextChange = true;
		_textField.setText(text);
		_isInternalTextChange = false;
	}
	
	public String getText()
	{
		return _textField.getText();
	}
	
	private void fireTextChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (TextChangedListener listener : _listeners)
		{
			listener.textChanged(event);
		}
	}
	
	public void addTextChangedListener(TextChangedListener listener)
	{
		_listeners.add(listener);
	}
	
	public void removeTextChangedListener(TextChangedListener listener)
	{
		_listeners.remove(listener);
	}
}
