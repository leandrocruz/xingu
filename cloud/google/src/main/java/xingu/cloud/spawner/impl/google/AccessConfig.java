package xingu.cloud.spawner.impl.google;

public class AccessConfig
{
	private String kind;
	
	private String name;
	
	private String natIP;
	
	private String type;

	public String getKind()
	{
		return kind;
	}

	public void setKind(String kind)
	{
		this.kind = kind;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNatIP()
	{
		return natIP;
	}

	public void setNatIP(String natIP)
	{
		this.natIP = natIP;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
