package xingu.node.console.command.impl;

import org.apache.commons.lang3.StringUtils;

import xingu.node.console.command.Command;
import xingu.node.console.command.Writer;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;

public class StopPulga
	implements Command<Void>
{
	@Inject
	private Container container;

	@Override
	public Void execute(String[] args, Writer writer)
		throws Exception
	{
		String name = args == null || args.length == 0 ? null: args[0];
		if(StringUtils.isEmpty(name))
		{
			System.out.println("Stopping All components");
			container.stop();
		}
		else
		{
			System.out.println("Stopping component '" + name + "'");
			Class<?> clazz = Class.forName(name);
			Object component = container.lookup(clazz);
			container.stopLifecycle(component);
			System.out.println("DONE");
		}
		return null;
	}
}
