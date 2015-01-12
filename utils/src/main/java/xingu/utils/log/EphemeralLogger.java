package xingu.utils.log;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.Marker;

import xingu.lang.NotImplementedYet;

public class EphemeralLogger
	implements Logger
{
	private String	name;
	
	private List<LogEvent> events = new ArrayList<>();
	
	public EphemeralLogger()
	{}
	
	public EphemeralLogger(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isTraceEnabled()
	{
		return true;
	}

	@Override
	public void trace(String msg)
	{
		events.add(new LogEventImpl(LogEvent.Type.TRACE, msg));
	}

	@Override
	public void trace(String format, Object arg)
	{
		events.add(new LogEventImpl(LogEvent.Type.TRACE, format, new Object[]{arg}));
	}

	@Override
	public void trace(String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void trace(String format, Object... arguments)
	{
		events.add(new LogEventImpl(LogEvent.Type.TRACE, format, arguments));
	}

	@Override
	public void trace(String msg, Throwable t)
	{
		events.add(new LogEventImpl(LogEvent.Type.TRACE, msg, t));
	}

	@Override
	public boolean isTraceEnabled(Marker marker)
	{
		return false;
	}

	@Override
	public void trace(Marker marker, String msg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void trace(Marker marker, String format, Object arg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t)
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean isDebugEnabled()
	{
		return true;
	}

	@Override
	public void debug(String msg)
	{
		events.add(new LogEventImpl(LogEvent.Type.DEBUG, msg));
	}

	@Override
	public void debug(String format, Object arg)
	{
		events.add(new LogEventImpl(LogEvent.Type.DEBUG, format, new Object[]{arg}));
	}

	@Override
	public void debug(String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void debug(String format, Object... arguments)
	{
		events.add(new LogEventImpl(LogEvent.Type.DEBUG, format, arguments));
	}

	@Override
	public void debug(String msg, Throwable t)
	{
		events.add(new LogEventImpl(LogEvent.Type.DEBUG, msg, t));
	}

	@Override
	public boolean isDebugEnabled(Marker marker)
	{
		return true;
	}

	@Override
	public void debug(Marker marker, String msg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void debug(Marker marker, String format, Object arg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void debug(Marker marker, String format, Object... arguments)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t)
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean isInfoEnabled()
	{
		return true;
	}

	@Override
	public void info(String msg)
	{
		events.add(new LogEventImpl(LogEvent.Type.INFO, msg));
	}

	@Override
	public void info(String format, Object arg)
	{
		events.add(new LogEventImpl(LogEvent.Type.INFO, format, new Object[]{arg}));
	}

	@Override
	public void info(String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void info(String format, Object... arguments)
	{
		events.add(new LogEventImpl(LogEvent.Type.INFO, format, arguments));
	}

	@Override
	public void info(String msg, Throwable t)
	{
		events.add(new LogEventImpl(LogEvent.Type.INFO, msg, t));
	}

	@Override
	public boolean isInfoEnabled(Marker marker)
	{
		return true;
	}

	@Override
	public void info(Marker marker, String msg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void info(Marker marker, String format, Object arg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void info(Marker marker, String format, Object... arguments)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void info(Marker marker, String msg, Throwable t)
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean isWarnEnabled()
	{
		return true;
	}

	@Override
	public void warn(String msg)
	{
		events.add(new LogEventImpl(LogEvent.Type.WARN, msg));
	}

	@Override
	public void warn(String format, Object arg)
	{
		events.add(new LogEventImpl(LogEvent.Type.WARN, format, new Object[]{arg}));
	}

	@Override
	public void warn(String format, Object... arguments)
	{
		events.add(new LogEventImpl(LogEvent.Type.WARN, format, arguments));
	}

	@Override
	public void warn(String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void warn(String msg, Throwable t)
	{
		events.add(new LogEventImpl(LogEvent.Type.WARN, msg, t));
	}

	@Override
	public boolean isWarnEnabled(Marker marker)
	{
		return true;
	}

	@Override
	public void warn(Marker marker, String msg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void warn(Marker marker, String format, Object arg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void warn(Marker marker, String format, Object... arguments)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t)
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean isErrorEnabled()
	{
		return true;
	}

	@Override
	public void error(String msg)
	{
		events.add(new LogEventImpl(LogEvent.Type.ERROR, msg));
	}

	@Override
	public void error(String format, Object arg)
	{
		events.add(new LogEventImpl(LogEvent.Type.ERROR, format, new Object[]{arg}));
	}

	@Override
	public void error(String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void error(String format, Object... arguments)
	{
		events.add(new LogEventImpl(LogEvent.Type.ERROR, format, arguments));
	}

	@Override
	public void error(String msg, Throwable t)
	{
		events.add(new LogEventImpl(LogEvent.Type.ERROR, msg, t));
	}

	@Override
	public boolean isErrorEnabled(Marker marker)
	{
		return true;
	}

	@Override
	public void error(Marker marker, String msg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void error(Marker marker, String format, Object arg)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void error(Marker marker, String format, Object... arguments)
	{
		throw new NotImplementedYet();
	}

	@Override
	public void error(Marker marker, String msg, Throwable t)
	{
		throw new NotImplementedYet();
	}

	public List<LogEvent> getEvents()
	{
		return events;
	}
}