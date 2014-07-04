package regexgolf2.ui.challengegenerator.configui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.controlsfx.control.RangeSlider;

public class RandomGeneratorConfigUI
{
	private Node _rootNode;
	private RangeSlider _doMatchSlider;
	private RangeSlider _dontMatchSlider;
	
	
	
	public RandomGeneratorConfigUI()
	{
		initUI();
	}
	
	
	
	private void initUI()
	{
		VBox vBox = new VBox();
		vBox.setSpacing(8);
		
		_doMatchSlider = createRangeSlider(0, 20, 0, 20);
		vBox.getChildren().add(createLabeledNode("Do Match Words", _doMatchSlider));
		
		_dontMatchSlider = createRangeSlider(0, 20, 0, 20);
		vBox.getChildren().add(createLabeledNode("Don't Match Words", _dontMatchSlider));
		
		_rootNode = vBox;
	}
	
	private RangeSlider createRangeSlider(int min, int max, int lower, int higher)
	{
		RangeSlider slider = new RangeSlider(min, max, lower, higher);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(5);
		slider.setMinorTickCount(5);
		slider.setSnapToTicks(true);
		return slider;
	}
	
	private Node createLabeledNode(String labelText, Node node)
	{
		Label description = new Label(labelText);
		
		VBox vb = new VBox();
		vb.setSpacing(2d);
		vb.getChildren().addAll(description, node);
		return vb;
	}
	
	public RangeSlider getDoMatchSlider()
	{
		return _doMatchSlider;
	}
	
	public RangeSlider getDontMatchSlider()
	{
		return _dontMatchSlider;
	}
	
	public Node getUINode()
	{
		return _rootNode;
	}
}
