package xingu.settings;

public interface SettingsManager
{
	Settings settingsFor(long owner)
		throws Exception;

	void store(long owner, Settings conf)
		throws Exception;
}
