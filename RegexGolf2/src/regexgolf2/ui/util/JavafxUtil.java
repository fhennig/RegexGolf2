package regexgolf2.ui.util;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class JavafxUtil
{
	/**
	 * Sets the given Node as content to the given AnchorPane
	 * @param content The Node that should be used as content, can be null
	 * @param ap The AnchorPane that the content should fill
	 */
	public static void setAsContent(Node content, AnchorPane ap)
	{
		ap.getChildren().clear();
		if (content == null)
			return;
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		ap.getChildren().add(content);
	}
	
	public static void addDebugBorder(Region region)
	{
		region.borderProperty().set(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	}
}
