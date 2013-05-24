package xingu.node.console.command.impl;

import xingu.node.console.command.Command;

public class GarbageCollection
	implements Command
{
	@Override
	public Object execute(String[] args)
		throws Exception
	{
		System.gc();
		return null;
	}
}