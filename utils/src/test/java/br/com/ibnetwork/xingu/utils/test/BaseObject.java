package br.com.ibnetwork.xingu.utils.test;

public class BaseObject
	implements IObject
{
	private long id;
	
	private String name;

	public long getId()
    {
    	return id;
    }

	public void setId(long id)
    {
    	this.id = id;
    }

	public String getName()
    {
    	return name;
    }

	public void setName(String name)
    {
    	this.name = name;
    }
}
