package xingu.cloud.spawner.impl.google.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Disk
{
	private boolean autoDelete;
	
	private boolean boot;
	
	private String deviceName;
	
	private int index;
	
	@JsonProperty("interface")
	private String iface;
	
	private String kind;
	
	private List<String> licenses;
	
	private String mode;
	
	private String source;
	
	private String type;

	public boolean isAutoDelete()
	{
		return autoDelete;
	}

	public void setAutoDelete(boolean autoDelete)
	{
		this.autoDelete = autoDelete;
	}

	public boolean isBoot()
	{
		return boot;
	}

	public void setBoot(boolean boot)
	{
		this.boot = boot;
	}

	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public String getIface()
	{
		return iface;
	}

	public void setIface(String iface)
	{
		this.iface = iface;
	}

	public String getKind()
	{
		return kind;
	}

	public void setKind(String kind)
	{
		this.kind = kind;
	}

	public List<String> getLicenses()
	{
		return licenses;
	}

	public void setLicenses(List<String> licenses)
	{
		this.licenses = licenses;
	}

	public String getMode()
	{
		return mode;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
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
