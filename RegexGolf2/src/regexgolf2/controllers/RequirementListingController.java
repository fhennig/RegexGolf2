package regexgolf2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private final BooleanProperty _editableProperty = new SimpleBooleanProperty();
	
	
	
	/**
	 * @param challengeProperty  a Property that is used for binding if it is not null.
	 * @param expectedMatchResult  the expectedMatchResult of the Requirements that should be displayed.
	 * @param editable  If the Requirements should be editable or not.
	 */
	public RequirementListingController(ObjectProperty<SolvableChallenge> challengeProperty,
			boolean expectedMatchResult) throws IOException
	{
		_expectedMatchResult = expectedMatchResult;
		_ui = new RequirementListUI();
		_ui.editableProperty().bind(_editableProperty);
		initUITitle();
		initButtonHandlers();
		initChallengeListener(); //Listener for Changes INSIDE the Challenge
		initChallengeChangedReaction(); //Change of the Challenge Property
		if (challengeProperty != null)
			_challenge.bind(challengeProperty);
	}
	
		
	
	private void initUITitle()
	{
		String title = _expectedMatchResult ? "Do Match" : "Don't Match";
		_ui.getTitleLabel().setText(title);
	}
	
	private void initButtonHandlers()
	{
		_ui.getAddButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				getChallenge().getChallenge().addRequirement(new Requirement(_expectedMatchResult));
				//TODO preselect the newly created requirement and also set editMode = true on cell
			}
		});
		_ui.getRemoveButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				Requirement r = _ui.getSelectedItem().getRequirement();
				getChallenge().getChallenge().removeRequirement(r);
			}
		});
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
				addRequirementToUI(r);
		}
		refreshListViewItems();
	}
	
	/**
	 * Creates a new RequirementItem with the given Requirement and adds
	 * the Item to the UI-List. The created Item is also returned.
	 */
	private RequirementItem addRequirementToUI(Requirement r)
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
		_ui.addRequirementItem(item);
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
		return _editableProperty;
	}
	
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
