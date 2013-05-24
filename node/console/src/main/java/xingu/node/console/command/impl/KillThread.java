package xingu.node.console.command.impl;

import java.util.Map;

import xingu.node.console.command.Command;

public class KillThread
	implements Command
{
	@Override
	public Object execute(String[] args)
		throws Exception
	{
		long wanted = Long.parseLong(args[0]);
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		for(Thread t : map.keySet())
		{
			long id = t.getId();
			if(wanted == id)
			{
				System.out.println("Interrupting " + t.getId() + " " + t.getName());
				t.interrupt();
				break;
			}
		}
		return null;
	}
}
