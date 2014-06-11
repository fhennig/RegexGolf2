package regexgolf2.controllers;

import java.io.IOException;
import java.util.EventObject;

import com.google.java.contract.Ensures;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.challengesolving.ChallengeSolvingUI;
import regexgolf2.ui.subcomponents.solutionediting.TextChangedListener;

public class ChallengeSolvingController
{
	private final ChallengeSolvingUI _ui;
	private final RequirementListingController _doMatchController;
	private final RequirementListingController _dontMatchController;
	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<>();
	private ObjectChangedListener _challengeListener;
	
	
	
	public ChallengeSolvingController() throws IOException
	{
		this(null);
	}
	
	public ChallengeSolvingController(SolvableChallenge challenge) throws IOException
	{
		_doMatchController = new RequirementListingController(_challenge, true, false);
		_dontMatchController = new RequirementListingController(_challenge, false, false);
		
		_ui = new ChallengeSolvingUI(_doMatchController.getUINode(), _dontMatchController.getUINode());

		initTextBox();
		initChallengeListener();
		initChallengeChangedReaction();
		
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
		_ui.getChallengeNameLabel().setText(text);
	}
	
	private void refreshScoreDisplay()
	{
		if (getChallenge() == null)
		{
			_ui.getScoreDisplayUI().setAmountCompliedRequirements(0);
			_ui.getScoreDisplayUI().setAmountRequirements(0);
			_ui.getScoreDisplayUI().setHighlight(false);
		}
		else
		{
			_ui.getScoreDisplayUI().setAmountCompliedRequirements(getChallenge().getAmountCompliedRequirements());
			_ui.getScoreDisplayUI().setAmountRequirements(getChallenge().getAmountRequirements());
			_ui.getScoreDisplayUI().setHighlight(getChallenge().isSolved());
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
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
