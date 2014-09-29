package xingu.settings;

import java.util.Map;

public interface Settings
{
	String getString(String key);

	int getInt(String key);

	void set(String key, String value);

	Map<String, Object> toMap();
}