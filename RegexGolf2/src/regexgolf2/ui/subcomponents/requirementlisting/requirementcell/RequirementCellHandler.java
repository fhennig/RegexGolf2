package regexgolf2.ui.subcomponents.requirementlisting.requirementcell;

import com.google.java.contract.Requires;

import regexgolf2.model.Requirement;

/**
 * Interface used by the RequirementCell to delegate interaction logic. 
 * The Cell does not handle any user interaction, but rather calls methods 
 * from this interface.
 */
public interface RequirementCellHandler
{
	/**
	 * Is called by the cell if the Requirement it represents changed.
	 * This is related to implementational specifics of the ListCell<T>.
	 * @param source  The cell calling the method
	 * @param requirement  The new Requirement
	 */
	@Requires({"source != null", "requirement != null"})
	void requirementChanged(RequirementCellUI source, Requirement requirement);
	
	/**
	 * This method is called by the cell if the word in the editable Label
	 * of the cell changed.
	 * @param source  The cell calling the method
	 * @param newWord  The new Word that is now displayed by the cell
	 */
	@Requires({
		"source != null",
		"newWord != null"
	})
	void requirementEdited(RequirementCellUI source, String newWord);
}
