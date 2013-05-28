package xingu.node.console.command.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;

import xingu.node.console.command.Command;
import xingu.node.console.command.Writer;

public class ListThreads
	implements Command<String>
{
	@Override
	public String execute(String[] args, Writer writer)
		throws Exception
	{
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

			sb.append(id).append("\t")
				.append(thread.isDaemon()).append("\t")
				.append(info.isSuspended()).append("\t\t")
				.append(info.getThreadState()).append(" \t")
				.append(info.getThreadName())
				.append("\n");
		}
		return sb.toString();
	}
}
