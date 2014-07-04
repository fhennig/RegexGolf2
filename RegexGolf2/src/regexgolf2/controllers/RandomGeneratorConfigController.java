package regexgolf2.controllers;

import javafx.scene.Node;
import regexgolf2.model.regexgenerator.RandomGeneratorConfig;
import regexgolf2.ui.challengegenerator.configui.RandomGeneratorConfigUI;

public class RandomGeneratorConfigController
{
	private final RandomGeneratorConfigUI _ui = new RandomGeneratorConfigUI();
	private RandomGeneratorConfig _config;



	public RandomGeneratorConfigController()
	{
		initHandlers();
	}



	public void setConfig(RandomGeneratorConfig config)
	{
		_config = config;
		if (_config != null)
			setSliders(_config.getDoMatchLowerBound(), _config.getDoMatchHigherBound(),
				_config.getDontMatchLowerBound(), _config.getDontMatchHigherBound());
		else
			setSliders(0, 0, 0, 0);
	}

	private void initHandlers()
	{
		_ui.getDoMatchSlider().lowValueProperty().addListener((o, oV, nV) -> updateConfig());
		_ui.getDoMatchSlider().highValueProperty().addListener((o, oV, nV) -> updateConfig());
		_ui.getDontMatchSlider().lowValueProperty().addListener((o, oV, nV) -> updateConfig());
		_ui.getDontMatchSlider().highValueProperty().addListener((o, oV, nV) -> updateConfig());
	}

	private void setSliders(int doMatchLow, int doMatchHigh, int dontMatchLow, int dontMatchHigh)
	{
		_ui.getDoMatchSlider().setLowValue(doMatchLow);
		_ui.getDoMatchSlider().setHighValue(doMatchHigh);
		_ui.getDontMatchSlider().setLowValue(dontMatchLow);
		_ui.getDontMatchSlider().setHighValue(dontMatchHigh);
	}

	private void updateConfig()
	{
		if (_config == null)
			return;

		int doMatchLow = (int) Math.round(_ui.getDoMatchSlider().getLowValue());
		int doMatchHigh = (int) Math.round(_ui.getDoMatchSlider().getHighValue());
		int dontMatchLow = (int) Math.round(_ui.getDontMatchSlider().getLowValue());
		int dontMatchHigh = (int) Math.round(_ui.getDontMatchSlider().getHighValue());

		_config.setDoMatchLowerBound(doMatchLow);
		_config.setDoMatchHigherBound(doMatchHigh);
		_config.setDontMatchLowerBound(dontMatchLow);
		_config.setDontMatchHigherBound(dontMatchHigh);
	}

	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
