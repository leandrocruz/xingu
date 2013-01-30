package xingu.journal.impl;

import xingu.journal.Journal;
import xingu.journal.PatternInterpolator;

public abstract class JournalSupport
	implements Journal
{
	
	private boolean isDebugEnabled()
	{
		return false;
	}

	private boolean isInfoEnabled()
	{
		return true;
	}
	
	@Override
	public void debug(String string)
		throws Exception
	{
		if(isDebugEnabled())
		{
			append(Level.DEBUG, string);
		}
	}

	@Override
	public void debug(String pattern, Object... args)
		throws Exception
	{
		if(isDebugEnabled())
		{
			append(Level.DEBUG, pattern, args);
		}
	}
	
	@Override
	public void info(String string)
		throws Exception
	{
		if(isInfoEnabled())
		{
			append(Level.INFO, string);
		}
	}

	@Override
	public void info(String pattern, Object... args)
		throws Exception
	{
		if(isInfoEnabled())
		{
			append(Level.INFO, pattern, args);
		}
	}
	
	protected void append(Level level, String pattern, Object... args)
		throws Exception
	{
		String s = PatternInterpolator.interpolate(pattern, args);
		append(level, s);
	}

	protected abstract void append(Level level, String string)
		throws Exception;
}
