package regexgolf2.controllers;

import java.util.EventObject;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.changetracking.PersistenceState;
import regexgolf2.ui.subcomponents.challengelisting.challengecell.ChallengeItem;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeItemController
{
	private final SolvableChallenge _challenge;
	private final PersistenceState _persistenceState;
	private ChallengeItem _item;
	private ObjectChangedListener _listener;
	
	
	
	
	@Requires({
		"challenge != null",
		"pState != null",
		"pState.getObservedItem() == challenge"
	})
	public ChallengeItemController(SolvableChallenge challenge, PersistenceState pState)
	{
		_challenge = challenge;
		_persistenceState = pState;
		initItem();
		initListener();
	}
	
	
	
	private void initItem()
	{
		_item = new ChallengeItem(_challenge.getChallenge().getName(),
				_challenge.getAmountRequirements(),
				_challenge.getAmountCompliedRequirements(),
				_persistenceState.isChanged(),
				_challenge.isSolved());
	}
	
	private void initListener()
	{
		_listener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				_item.update(_challenge.getChallenge().getName(),
						_challenge.getAmountRequirements(),
						_challenge.getAmountCompliedRequirements(),
						_persistenceState.isChanged(),
						_challenge.isSolved());
			}
		};
		_challenge.addObjectChangedListener(_listener);
		_persistenceState.addObjectChangedListener(_listener);
	}
	
	/**
	 * Returns the Challenge that is represented by the ChallengeItem
	 * associated with this Controller.
	 */
	@Ensures("result != null")
	public SolvableChallenge getChallenge()
	{
		return _challenge;
	}
	
	/**
	 * Returns the ChallengeItem that is controlled by this Controller.
	 */
	@Ensures("result != null")
	public ChallengeItem getItem()
	{
		return _item;
	}
	
	/**
	 * This method makes the Controller unsubscibe from the PersistenceState
	 * and Challenge it previously listened.
	 * That way, references to the Controller are removed and it can be 
	 * garbage collected.
	 */
	public void discard()
	{
		//XXX actually, this method will only be called, if the challenge
		//and persistenceState will also be un-referenced in the near future.
		//If this is allways the case, this method would be obsolete.
		_challenge.removeObjectChangedListener(_listener);
		_persistenceState.removeObjectChangedListener(_listener);
	}
}
