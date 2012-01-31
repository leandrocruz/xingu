package xingu.store;

public class Pojo
	implements PersistentBean
{
	private long id;
	
	private String name;

	public Pojo()
	{}
	
	public Pojo(long id, String name)
    {
		this.id = id;
		this.name = name;
    }

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

	public String getDisplay()
    {
	    return null;
    }
}
