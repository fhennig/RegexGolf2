package regexgolf2.ui.subcomponents.requirementlisting.requirementcell;

import javafx.scene.control.ListCell;
import regexgolf2.model.Requirement;

/**
 * RequirementListUI takes an Object as a parameter in its constructor
 * that implements this interface.
 * The object implementing this interface is responsible for cell creation,
 * The RequirementListUI does not take care of that itself.
 * This is necessary, because Cells need a Controller and the Controller
 * should not be instantiated in a UI class. 
 */
public interface RequirementCellFactory
{
	public ListCell<Requirement> createCell();
}
