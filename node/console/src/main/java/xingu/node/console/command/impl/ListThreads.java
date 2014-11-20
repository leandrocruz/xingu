package xingu.node.console.command.impl;

import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import xingu.node.console.command.Command;
import xingu.node.console.command.Writer;

public class ListThreads
	implements Command<String>
{
	@Override
	public String execute(String[] args, Writer writer)
		throws Exception
	{
		String nameToMach   = null;
		String stateToMatch = null;
		if(args != null && args.length > 0)
		{
			for(String arg : args)
			{
				if(arg.startsWith("n:"))
				{
					nameToMach = arg.substring(2);
				}
				else if(arg.startsWith("s:"))
				{
					stateToMatch = arg.substring(2);
				}
			}
		}

		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		
		ThreadMXBean manager = ManagementFactory.getThreadMXBean();
		ThreadInfo[] infos = manager.dumpAllThreads(true, true);
		StringBuffer sb = new StringBuffer();
		sb.append("ID\tDAEMON\tSUSPENDED\tSTATE\t\tNAME\n");
		for (ThreadInfo info : infos)
		{
			Thread thread = null;
			long id = info.getThreadId();
			for(Thread t : map.keySet())
			{
				if(t.getId() == id)
				{
					thread = t;
					break;
				}
			}

			String name = info.getThreadName();
			boolean include = true;
			if(nameToMach != null)
			{
				include = StringUtils.indexOfIgnoreCase(name, nameToMach) >= 0;
			}
			
			State state = info.getThreadState();
			if(stateToMatch != null)
			{
				include = include && state.name().equalsIgnoreCase(stateToMatch);
			}

			if(include)
			{
				sb
					.append(id).append("\t")
					.append(thread != null ? thread.isDaemon() : "NA").append("\t")
					.append(info.isSuspended()).append("\t\t")
					.append(state).append(" \t")
					.append(name)
					.append("\n");
			}
		}
		return sb.toString();
	}
}
