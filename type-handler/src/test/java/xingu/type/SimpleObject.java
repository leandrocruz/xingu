package xingu.type;

import java.util.Date;


public class SimpleObject
{
	private int			intField;

	private String		stringField;

	private String[]	arrayOfStrings;
	
	@Param(name="date", format="ddMMyyyy")
	private Date		date;

	public int getIntField()
	{
		return intField;
	}

	public void setIntField(int intField)
	{
		this.intField = intField;
	}

	public String getStringField()
	{
		return stringField;
	}

	public void setStringField(String stringField)
	{
		this.stringField = stringField;
	}

	public String[] getArrayOfStrings()
	{
		return arrayOfStrings;
	}

	public void setArrayOfStrings(String[] arrayOfStrings)
	{
		this.arrayOfStrings = arrayOfStrings;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}
}