package xingu.node.console.command.impl;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;

import xingu.node.console.command.Command;

import org.apache.commons.lang3.StringUtils;

public class StopPulga
	implements Command<Void>
{
	@Inject
	private Container container;

	@Override
	public Void execute(String[] args)
		throws Exception
	{
		String name = args.length > 0 ? args[0] : null;
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
