package xingu.cloud.spawner.impl.google.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateJsonReply
{
	private String					id;

	private String					name;

	private String					status;

	private String					zone;

	private String					kind;

	private String					machineType;

	private Date					creationTimestamp;

	private String					selfLink;

	private boolean					canIpForward;

	private Scheduling				scheduling;

	private Map<String, String>		tags;

	private Map<String, Object>		metadata;

	private List<Disk>				disks;

	private List<NetworkInterface>	networkInterfaces;

	private List<ServiceAccount>	serviceAccounts;

	public boolean isCanIpForward()
	{
		return canIpForward;
	}

	public void setCanIpForward(boolean canIpForward)
	{
		this.canIpForward = canIpForward;
	}

	public Date getCreationTimestamp()
	{
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp)
	{
		this.creationTimestamp = creationTimestamp;
	}

	public List<Disk> getDisks()
	{
		return disks;
	}

	public void setDisks(List<Disk> disks)
	{
		this.disks = disks;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getKind()
	{
		return kind;
	}

	public void setKind(String kind)
	{
		this.kind = kind;
	}

	public String getMachineType()
	{
		return machineType;
	}

	public void setMachineType(String machineType)
	{
		this.machineType = machineType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Map<String, Object> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata)
	{
		this.metadata = metadata;
	}

	public List<NetworkInterface> getNetworkInterfaces()
	{
		return networkInterfaces;
	}

	public void setNetworkInterfaces(List<NetworkInterface> networkInterfaces)
	{
		this.networkInterfaces = networkInterfaces;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getZone()
	{
		return zone;
	}

	public void setZone(String zone)
	{
		this.zone = zone;
	}

	public String getSelfLink()
	{
		return selfLink;
	}

	public void setSelfLink(String selfLink)
	{
		this.selfLink = selfLink;
	}

	public Scheduling getScheduling()
	{
		return scheduling;
	}

	public void setScheduling(Scheduling scheduling)
	{
		this.scheduling = scheduling;
	}

	public Map<String, String> getTags()
	{
		return tags;
	}

	public void setTags(Map<String, String> tags)
	{
		this.tags = tags;
	}

	public List<ServiceAccount> getServiceAccounts()
	{
		return serviceAccounts;
	}

	public void setServiceAccounts(List<ServiceAccount> serviceAccounts)
	{
		this.serviceAccounts = serviceAccounts;
	}
}
