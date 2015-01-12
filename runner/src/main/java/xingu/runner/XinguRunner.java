package xingu.runner;

import xingu.container.Container;
import xingu.container.ContainerUtils;
import xingu.factory.Factory;
import xingu.utils.ObjectUtils;


public class XinguRunner
{
	public static void main(String[] args)
		throws Exception
	{
		String className = null;
		if(args.length <=0 )
		{
			System.out.println("I need a main class");
			System.exit(-1);
		}
	
		className = args[0];
		System.out.println("Loading class '" + className + "'");
		Class<?> clazz = ObjectUtils.loadClass(className);
		if(!Main.class.isAssignableFrom(clazz))
		{
			System.out.println("Class '" + className + "' not an instance of Main");
			System.exit(-1);
		}
		
		String[] my = new String[args.length - 1];
		System.arraycopy(args, 1, my, 0, my.length);

		Container container = ContainerUtils.getContainer();
		Factory factory = container.lookup(Factory.class);
		Main main = (Main) factory.create(clazz);
		main.execute(my);
	}
}