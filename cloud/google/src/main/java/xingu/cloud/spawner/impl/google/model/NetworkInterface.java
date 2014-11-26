package xingu.cloud.spawner.impl.google.model;

import java.util.List;

public class NetworkInterface
{
	private List<AccessConfig> accessConfigs;
	
	private String name;
	
	private String network;
	
	private String networkIP;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNetwork()
	{
		return network;
	}

	public void setNetwork(String network)
	{
		this.network = network;
	}

	public String getNetworkIP()
	{
		return networkIP;
	}

	public void setNetworkIP(String networkIP)
	{
		this.networkIP = networkIP;
	}

	public List<AccessConfig> getAccessConfigs()
	{
		return accessConfigs;
	}

	public void setAccessConfigs(List<AccessConfig> accessConfigs)
	{
		this.accessConfigs = accessConfigs;
	}
}
