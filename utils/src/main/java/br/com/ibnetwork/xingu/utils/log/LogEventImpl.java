package br.com.ibnetwork.xingu.utils.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogEventImpl
	implements LogEvent
{
	private Date			date;

	private Type			type;

	private String			msg;

	private String			format;

	private List<Object>	args;

	private Throwable		error;

	public LogEventImpl()
	{}
	
	public LogEventImpl(Type type, String msg)
	{
		this.date = new Date();
		this.type = type;
		this.msg  = msg;
	}

	public LogEventImpl(Type type, String format, Object[] args)
	{
		this.date = new Date();
		this.type   = type;
		this.format = format;
		if(args != null)
		{
			this.args = new ArrayList<>();
			for(Object arg : args)
			{
				this.args.add(arg);
			}
		}
	}

	public LogEventImpl(Type type, String msg, Throwable error)
	{
		this.date = new Date();
		this.type  = type;
		this.msg   = msg;
		this.error = error;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public String getMessage()
	{
		return msg;
	}

	public void setMessage(String msg)
	{
		this.msg = msg;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public List<Object> getArgs()
	{
		return args;
	}

	public void setArgs(List<Object> args)
	{
		this.args = args;
	}

	public Throwable getError()
	{
		return error;
	}

	public void setError(Throwable error)
	{
		this.error = error;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}
}