package br.com.ibnetwork.xingu.utils.inspector;

import java.util.Date;

public class SimpleObject
{
	int    anInt;

	String aString;
	
	String bString;
	
	Date date;

	public SimpleObject(int i, String string)
	{
		this(i, string, new Date());
	}
	
	public SimpleObject(int i, String string, Date date)
	{
		this.anInt   = i;
		this.aString = string;
		this.bString = string;
		this.date    = date;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof SimpleObject))
		{
			return false;
		}
		
		SimpleObject other = (SimpleObject) obj;
		return other.anInt == anInt 
				&& other.aString.equals(aString)
				&& other.bString.equals(bString)
				&& other.date.equals(date);
	}
}