package regexgolf2.ui.subcomponents.requirementlisting.requirementcell;

import javafx.scene.control.ListCell;
import regexgolf2.model.Requirement;

public interface RequirementCellFactory
{
	public ListCell<Requirement> createCell();
}
