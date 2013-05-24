package xingu.node.console.command.impl;

import xingu.node.console.command.Command;

public class Exit
	implements Command
{
	@Override
	public Object execute(String[] args)
		throws Exception
	{
		System.exit(0);
		return null;
	}
}