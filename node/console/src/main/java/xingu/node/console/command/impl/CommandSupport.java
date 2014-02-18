package xingu.node.console.command.impl;

import xingu.node.console.command.Command;
import xingu.node.console.command.Writer;

public abstract class CommandSupport
	implements Command<String>
{
	@Override
	public String execute(String[] args, final Writer writer)
		throws Exception
	{
		boolean argsOk = checkArgs(args);
		if(!argsOk)
		{
			return usage();
		}
		
		String result = doWork(args, writer);
		return result;
	}

	protected boolean checkArgs(String[] args)
	{
		return args != null;
	}

	protected abstract String doWork(String[] args, Writer writer)
		throws Exception;

	protected abstract String usage();
}