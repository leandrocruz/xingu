package xingu.runner;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.ObjectUtils;


public class XinguRunner
{
	public static void main(String[] args)
		throws Exception
	{
		if(args.length == 0)
		{
			throw new NotImplementedYet("I need a main class");
		}

		if(args.length > 1)
		{
			throw new NotImplementedYet("Todo: parse args");
		}

		String className = args[0];
		System.out.println("Loading class '" + className + "'");
		Class<?> clazz = ObjectUtils.loadClass(className);
		if(!Runnable.class.isAssignableFrom(clazz))
		{
			System.out.println("Class '" + className + "' not a runnable");
			System.exit(-1);
		}

		Container container = ContainerUtils.getContainer();
		
		Factory factory = container.lookup(Factory.class);
		Runnable runnable = (Runnable) factory.create(clazz);
		runnable.run();
	}
}