package regexgolf2.controllers;

import java.io.IOException;
import java.util.EventObject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Requirement;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.challengesolving.ChallengeSolvingUI;
import regexgolf2.ui.subcomponents.solutionediting.TextChangedListener;

import com.google.java.contract.Ensures;

public class ChallengeSolvingController
{
	private final BooleanProperty _editable = new SimpleBooleanProperty();
	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<>();
	private final ChallengeSolvingUI _ui;
	private final RequirementListingController _doMatchController;
	private final RequirementListingController _dontMatchController;
	private ObjectChangedListener _challengeListener;
	
	
	
	public ChallengeSolvingController() throws IOException
	{
		this(null);
	}
	
	public ChallengeSolvingController(SolvableChallenge challenge) throws IOException
	{
		_doMatchController = new RequirementListingController(_challenge, true);
		_dontMatchController = new RequirementListingController(_challenge, false);
		
		_doMatchController.editableProperty().bind(_editable);
		_dontMatchController.editableProperty().bind(_editable);
		
		_ui = new ChallengeSolvingUI(_doMatchController.getUINode(), _dontMatchController.getUINode());
		_ui.editableProperty().bind(_editable);

		initTextBox();
		initChallengeNameUI();
		initChallengeListener();
		initChallengeChangedReaction();
		initAddButtonHandlers();
		
		setChallenge(challenge); //TODO possibly move to the end of ctor
		
		refreshUI();
	}
	
	private void initTextBox()
	{
		_ui.getSolutionEditingUI().addTextChangedListener(new TextChangedListener()
		{
			@Override
			public void textChanged(EventObject event)
			{
				reactToInputChanged();
			}
		});
	}
	
	private void initChallengeNameUI()
	{
		_ui.getChallengeTitleUI().textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue)
			{
				if (getChallenge() != null)
					getChallenge().getChallenge().setName(newValue);
			}
		});
	}
	
	private void initAddButtonHandlers()
	{
		_ui.getAddDoMatchBtn().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				getChallenge().getChallenge().addRequirement(new Requirement(true));
			}
		});
		
		_ui.getAddDontMatchBtn().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				getChallenge().getChallenge().addRequirement(new Requirement(false));
			}
		});
	}
	
	private void initChallengeListener()
	{
		_challengeListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				refreshUI();
			}
		};
	}
	
	private void initChallengeChangedReaction()
	{
		_challenge.addListener(new ChangeListener<SolvableChallenge>()
		{
			@Override
			public void changed(
					ObservableValue<? extends SolvableChallenge> observable,
					SolvableChallenge oldValue, SolvableChallenge newValue)
			{
				if (oldValue != null)
					oldValue.removeObjectChangedListener(_challengeListener);
				if (newValue != null)
					newValue.addObjectChangedListener(_challengeListener);
				refreshUI();
			}
		});
	}
	
	private void reactToInputChanged()
	{
		if (getChallenge() == null)
			return;
		getChallenge().getSolution().trySetSolution(_ui.getSolutionEditingUI().getText());
	}
	
	private void refreshUI()
	{
		refreshScoreDisplay();
		refreshSolutionTextBox();
		refreshChallengeNameLabel();
	}
	
	private void refreshChallengeNameLabel()
	{
		String text = (getChallenge() == null ? "" : getChallenge().getChallenge().getName());
		_ui.getChallengeTitleUI().setText(text);
	}
	
	private void refreshScoreDisplay()
	{
		if (getChallenge() == null)
		{
			_ui.getScoreDisplayUI().setAmountCompliedRequirements(0);
			_ui.getScoreDisplayUI().setAmountRequirements(0);
			_ui.getScoreDisplayUI().setHighlighted(false);
		}
		else
		{
			_ui.getScoreDisplayUI().setAmountCompliedRequirements(getChallenge().getAmountCompliedRequirements());
			_ui.getScoreDisplayUI().setAmountRequirements(getChallenge().getAmountRequirements());
			_ui.getScoreDisplayUI().setHighlighted(getChallenge().isSolved());
		}
	}
	
	private void refreshSolutionTextBox()
	{
		String text = (getChallenge() == null ? "" : getChallenge().getSolution().getSolution());
		_ui.getSolutionEditingUI().setText(text);
	}
	
	public SolvableChallenge getChallenge()
	{
		return _challenge.get();
	}
	
	public void setChallenge(SolvableChallenge challenge)
	{
		_challenge.set(challenge);
	}
	
	@Ensures("result != null")
	public ObjectProperty<SolvableChallenge> challengeProperty()
	{
		return _challenge;
	}
	
	public boolean isEditable()
	{
		return _editable.get();
	}
	
	public void setEditable(boolean editable)
	{
		_editable.set(editable);
	}
	
	public BooleanProperty editableProperty()
	{
		return _editable;
	}
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
