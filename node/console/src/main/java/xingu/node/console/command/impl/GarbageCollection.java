package xingu.node.console.command.impl;

import xingu.node.console.command.Command;

public class GarbageCollection
	implements Command<Void>
{
	@Override
	public Void execute(String[] args)
		throws Exception
	{
		System.gc();
		return null;
	}
}