package br.com.ibnetwork.xingu.utils;

import java.util.ArrayList;
import java.util.List;

public class ArgsUtils
{
	public static String[] norm(String[] args)
	{
		return norm(args, "'");
	}
	
	public static String[] norm(String[] args, String delimiter)
	{
		if(args == null || args.length == 0)
		{
			return null;
		}

		List<String> result  = new ArrayList<String>(args.length);
		String       tmp     = null;
		for(String arg : args)
		{
			if(tmp != null)
			{
				tmp = tmp + " " + arg;
				if(arg.endsWith(delimiter))
				{
					arg     = tmp.substring(1, tmp.length() - 1);
					tmp     = null;
				}
				else
				{
					continue;
				}
			}
			if(arg.startsWith(delimiter))
			{
				tmp     = arg;
				continue;
			}
			result.add(arg);
		}
		return result.toArray(StringUtils.EMPTY_ARRAY);
	}
}