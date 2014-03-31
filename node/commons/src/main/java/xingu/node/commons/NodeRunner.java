package xingu.node.commons;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import xingu.node.commons.universe.Universe;
import xingu.node.commons.universe.Universes;
import xingu.node.commons.universe.impl.UniverseImpl;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.lang.ThreadBlocker;
import br.com.ibnetwork.xingu.utils.io.FileUtils;

public class NodeRunner
{
	private static final String	ENV_PARAM		= "-env";

	private static final String	CONFIG_PARAM	= "-config=";
	
	private static final String	LOOKUP_PARAM	= "-lookup=";

	private final ThreadBlocker	blocker			= new ThreadBlocker();

	public void configure(String[] args)
		throws Exception
	{
		String name   = null;
		String lookup = null;
		if(args.length > 0)
		{
			for(String arg : args)
			{
				if(ENV_PARAM.equals(arg))
				{
					printEnvironment();
				}
				else if(arg.startsWith(CONFIG_PARAM))
				{
					name = arg.substring(CONFIG_PARAM.length());
				}
				else if(arg.startsWith(LOOKUP_PARAM))
				{
					lookup = arg.substring(LOOKUP_PARAM.length());
				}

			}
		}
		
		if(name == null || name.length() == 0)
		{
			name = ContainerUtils.getFileName();
		}
		System.out.println("Pulga Config: " + name);
		InputStream is        = FileUtils.toInputStream(name);
		Container   container = ContainerUtils.getContainer(is, false);

		Universes   universes = container.lookup(Universes.class);
		ClassLoader cl        = Thread.currentThread().getContextClassLoader();
		Universe    universe  = new UniverseImpl(Universe.SYSTEM, container, cl);
		universes.register(universe);
		
		container.start();
		
		if(lookup != null)
		{
			Class<?> clazz = cl.loadClass(lookup);
			container.lookup(clazz);
		}
	}

	public void start()
	{
		blocker.createThreadAndHold();
	}

	public void stop()
	{
		ContainerUtils.reset();
		blocker.letGo();
	}

	public static void main(String[] args)
		throws Exception
	{
		NodeRunner runner = new NodeRunner();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(runner));
		runner.configure(args);
		runner.start();
	}

	public void printEnvironment()
	{
		System.out.println("-- Environment Variables --");
		Map<String, String> env = System.getenv();
		for(String key : env.keySet())
		{
			System.out.println(key + " = " + env.get(key));
		}

		System.out.println("-- System Properties --");
		Properties props = System.getProperties();
		for(Object key : props.keySet())
		{
			System.out.println(key + " = " + props.get(key));
		}
	}
}

class ShutdownHook
	extends Thread
{
	private NodeRunner	runner;

	public ShutdownHook(NodeRunner runner)
	{
		this.runner = runner;
	}

	@Override
	public void run()
	{
		runner.stop();
	}
}
