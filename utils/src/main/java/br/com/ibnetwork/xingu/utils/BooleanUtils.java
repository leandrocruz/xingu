package br.com.ibnetwork.xingu.utils;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class BooleanUtils
{
	public static Boolean toBoolean(Object value)
	{
		if(value instanceof String)
		{
			String input = (String) value;
			return toBoolean(input);
		}
		else if(value instanceof Number)
		{
			Number input = (Number) value;
			return toBoolean(input);
		}
		else
		{
			throw new NotImplementedYet();
		}
	}

	public static Boolean toBoolean(Number value)
	{
		throw new NotImplementedYet();
	}

	public static Boolean toBoolean(String value)
	{
		return toBoolean(value, null);
	}
	
	public static Boolean toBoolean(String value, Boolean whenMissing)
	{
		switch(value)
		{
			case "1":
			case "true":
			case "y":
			case "s":
			case "yes":
			case "sim":
				return Boolean.TRUE;
			
			case "0":
			case "false":
			case "n":
			case "no":
			case "não":
				return Boolean.FALSE;

			default:
				return whenMissing;
		}
	}
}