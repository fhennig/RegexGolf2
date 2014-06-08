package regexgolf2.ui;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class JavafxUtil
{
	public static void setAsContent(Node content, AnchorPane ap)
	{
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		ap.getChildren().add(content);
	}
}
