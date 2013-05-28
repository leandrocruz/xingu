package xingu.node.console.command.impl;

import xingu.node.console.command.Command;
import xingu.node.console.command.Writer;

public class GarbageCollection
	implements Command<Void>
{
	@Override
	public Void execute(String[] args, Writer writer)
		throws Exception
	{
		System.gc();
		return null;
	}
}