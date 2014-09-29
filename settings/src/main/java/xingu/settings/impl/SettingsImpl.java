package xingu.settings.impl;

import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;

import xingu.settings.Settings;

public class SettingsImpl
	implements Settings
{
	private Map<String, Object>	map;

	private JXPathContext		ctx;

	public SettingsImpl(Map<String, Object> map)
	{
		this.map   = map;
		this.ctx   = JXPathContext.newContext(map);
	}

	@Override
	public String getString(String expression)
	{
		Object value = ctx.getValue(expression);
		return value.toString();
	}

	@Override
	public void set(String expression, String value)
	{
		ctx.setValue(expression, value);
	}

	@Override
	public int getInt(String expression)
	{
		Number value = (Number) ctx.getValue(expression);
		return value.intValue();
	}

	public Map<String, Object> getMap()
	{
		return map;
	}

	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}

	@Override
	public Map<String, Object> toMap()
	{
		return map;
	}
}