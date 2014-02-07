package xingu.maze;

public class Args
{
	private String[] args;
	
	public Args(String[] args)
	{
		this.args = args;
	}

	public boolean has(String name)
	{
		String value = get(name);
		return value != null;
	}

	
	public String get(String wanted)
	{
		if(args.length == 0 || wanted == null || wanted.length() == 0)
		{
			return null;
		}
		
		for(String arg : args)
		{
			String name = stripe(arg);
			if(name.equals(wanted))
			{
				String value = valueFrom(arg);
				return value == null ? name : value;
			}
		}
		return null;

	}

	private String valueFrom(String arg)
	{
		int idx = arg.indexOf("=");
		if(idx < 0)
		{
			//simple argument
			return null;
		}
		String value = arg.substring(idx + 1);
		return value;
	}

	private String stripe(String arg)
	{
		int idx = arg.indexOf("=");
		if(idx < 0)
		{
			//simple argument
			return arg.substring(1); 
		}
		else
		{
			//argument with value
			return arg.substring(1, idx);
		}
	}

	public String[] toArray()
	{
		return args;
	}

	public int size()
	{
		return args != null ? args.length : 0;
	}
}
