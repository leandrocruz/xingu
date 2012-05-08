package xingu.servlet.command.impl;

import xingu.servlet.command.Command;

public class CommandSupport
	implements Command
{
	private long id;
	
	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public void setId(long id)
	{
		this.id = id;
	}
}
