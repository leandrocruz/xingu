package xingu.node.console.command.impl;

import xingu.container.Container;
import xingu.container.Inject;
import xingu.node.console.command.Command;
import xingu.node.console.command.Writer;

public class ListPulgaComponents
	implements Command<String>
{
	@Inject
	private Container container;

	@Override
	public String execute(String[] args, Writer writer)
		throws Exception
	{
		java.util.List<Class<?>> roles = container.binder().getRoles();
		StringBuffer sb = new StringBuffer();
		sb.append("ROLES\n");
		for (Class<?> role : roles)
		{
			sb.append(role.getName()).append("\n");
		}
		return sb.toString();
	}
}
