package br.com.ibnetwork.xingu.utils.type;

public class WithParam
{
	@Param(name="x")
	private int intField;
	
	@Param(name="y")
	private String stringField;

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
}
