package xingu.node.console.impl;

import java.util.HashMap;
import java.util.Map;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.node.console.RemoteConsoleRegistry;
import xingu.node.console.command.Command;
import xingu.utils.ObjectUtils;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

public class RemoteConsoleRegistryImpl
	implements RemoteConsoleRegistry, Configurable
{
	@Inject
	private Factory factory;

	private Map<String, CommandConfig> commandByName = new HashMap<String, CommandConfig>();
	
	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		Configuration[] commands = conf.getChild("commands").getChildren("cmd");
		for (Configuration configuration : commands)
		{
			CommandConfig cmd = commandFrom(configuration);
			commandByName.put(cmd.name, cmd);
		}
	}

	private CommandConfig commandFrom(Configuration conf)
		throws ConfigurationException
	{
		String        name   = conf.getAttribute("name");
		String        clazz  = conf.getAttribute("class");
		CommandConfig result = new CommandConfig(name, clazz, conf);
		return result;
	}

	@Override
	public Command by(String name)
	{
		CommandConfig conf = commandByName.get(name);
		if(conf == null)
		{
			return null;
		}
		
		Command cmd = conf.cmd;
		if(cmd == null)
		{
			Class<?> clazz = ObjectUtils.loadClass(conf.clazz);
			cmd = (Command) factory.create(clazz, conf.conf);
			conf.cmd = cmd;
		}
		return cmd;
	}
}


class CommandConfig
{
	final String		name;

	final String		clazz;

	final Configuration	conf;

	Command				cmd;

	public CommandConfig(String name, String clazz, Configuration conf)
	{
		this.name  = name;
		this.clazz = clazz;
		this.conf  = conf;
	}
}