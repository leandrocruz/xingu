package xingu.inspector;

import java.util.Date;

public class UnregisteredObject
{
	int    anInt;

	String aString;
	
	String bString;
	
	Date date;

	public UnregisteredObject()
	{}
	
	public UnregisteredObject(int i, String string)
	{
		this(i, string, new Date());
	}
	
	public UnregisteredObject(int i, String string, Date date)
	{
		this.anInt   = i;
		this.aString = string;
		this.bString = string;
		this.date    = date;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof UnregisteredObject))
		{
			return false;
		}
		
		UnregisteredObject other = (UnregisteredObject) obj;
		return other.anInt == anInt 
				&& other.aString.equals(aString)
				&& other.bString.equals(bString)
				&& other.date.equals(date);
	}
}