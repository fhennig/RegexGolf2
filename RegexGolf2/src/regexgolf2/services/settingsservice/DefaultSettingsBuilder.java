package regexgolf2.services.settingsservice;


class DefaultSettingsBuilder
{
	public SettingsImpl getDefaultSettings()
	{
		return new SettingsImpl(
				"defaultDBPath"
				);
	}
}
