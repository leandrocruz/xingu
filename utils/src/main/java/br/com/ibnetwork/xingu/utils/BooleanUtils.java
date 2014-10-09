package br.com.ibnetwork.xingu.utils;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class BooleanUtils
{
	public Boolean toBoolean(Object value)
	{
		if(value instanceof String)
		{
			switch((String)value)
			{
				case "1":
				case "true":
				case "y":
				case "s":
				case "sim":
					return Boolean.TRUE;
					
				default:
					return Boolean.FALSE;
			}
		}
		else
		{
			throw new NotImplementedYet();
		}
	}
}