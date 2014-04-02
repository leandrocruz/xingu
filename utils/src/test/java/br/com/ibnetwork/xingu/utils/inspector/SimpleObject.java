package br.com.ibnetwork.xingu.utils.inspector;

public class SimpleObject
{
	int    anInt;

	String aString;
	
	String bString;

	public SimpleObject(int i, String string)
	{
		this.anInt   = i;
		this.aString = string;
		this.bString = string;
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
				&& other.bString.equals(bString);
	}
}