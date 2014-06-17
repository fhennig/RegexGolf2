package regexgolf2.controllers;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Requirement;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.subcomponents.requirementlisting.RequirementListUI;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementItem;

/**
 * The RequirementListingController is responsible for displaying
 * Requirements from a Challenge.
 * It uses a ListView to display the Requirements and reacts instantly
 * to changes in the Challenge.
 */
public class RequirementListingController
{
	private final RequirementListUI _ui;
	private ObjectChangedListener _challengeListener;
	
	private final boolean _expectedMatchResult;
	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<>();
	
	
	
	/**
	 * @param challengeProperty  a Property that is used for binding if it is not null.
	 * @param expectedMatchResult  the expectedMatchResult of the Requirements that should be displayed.
	 * @param editable  If the Requirements should be editable or not.
	 */
	public RequirementListingController(ObjectProperty<SolvableChallenge> challengeProperty,
			boolean expectedMatchResult)
	{
		_expectedMatchResult = expectedMatchResult;
		_ui = new RequirementListUI();
		initChallengeListener(); //Listener for Changes INSIDE the Challenge
		initChallengeChangedReaction(); //Change of the Challenge Property
		if (challengeProperty != null)
			_challenge.bind(challengeProperty);
	}
	
		
	
	/**
	 * Initializes the ChallengeListener that refreshes the UI
	 * if the Challenge changed.
	 */
	private void initChallengeListener()
	{
		_challengeListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				if (!(getChallenge() != null && getChallenge().equals(event.getSource())))
					throw new IllegalStateException("Challenge: " + _challenge + "\n" +
													"Event-Source: " + event.getSource());
				
				refreshRequirementListUI();
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
				refreshRequirementListUI();
			}
		});
	}
	
	private void refreshRequirementListUI()
	{
		List<Requirement> newRequirementList;
		if (getChallenge() != null)
			newRequirementList = getChallenge().getRequirements(_expectedMatchResult);
		else
			newRequirementList = new ArrayList<>();
		
		List<RequirementItem> toRemove = new ArrayList<>();
		for (RequirementItem item : _ui.getItems())
		{
			if (!newRequirementList.contains(item.getRequirement()))
				toRemove.add(item);
		}
		_ui.getItems().removeAll(toRemove);
		
		for (Requirement r : newRequirementList)
		{
			boolean itemExits = false;
			for (RequirementItem item : _ui.getItems())
			{
				if (item.getRequirement().equals(r))
				{
					itemExits = true;
					break;
				}
			}
			if (!itemExits)
				_ui.addRequirementItem(createRequirementItem(r));
		}
		refreshListViewItems();
	}
	
	private RequirementItem createRequirementItem(Requirement r)
	{
		final RequirementItem item = new RequirementItem(r, getChallenge().isComplied(r));
		item.wordProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue)
			{
				item.getRequirement().setWord(newValue);
			}
		});
		return item;
	}
	
	private void refreshListViewItems()
	{
		for (RequirementItem item : _ui.getItems())
		{
			item.setWord(item.getRequirement().getWord());
			item.setComplied(getChallenge().isComplied(item.getRequirement()));
		}
	}
	
	public SolvableChallenge getChallenge()
	{
		return _challenge.get();
	}
	
	public void setChallenge(SolvableChallenge challenge)
	{
		_challenge.set(challenge);
	}
	
	public ObjectProperty<SolvableChallenge> challengeProperty()
	{
		return _challenge;
	}
	
	public BooleanProperty editableProperty()
	{
		return _ui.editableProperty();
	}
	
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
