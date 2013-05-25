package xingu.node.console.command.impl;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;

import xingu.node.console.command.Command;

public class ListPulgaComponents
	implements Command<String>
{
	@Inject
	private Container container;

	@Override
	public String execute(String[] args)
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
