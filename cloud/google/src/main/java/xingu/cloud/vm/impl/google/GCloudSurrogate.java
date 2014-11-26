package xingu.cloud.vm.impl.google;

import br.com.ibnetwork.xingu.utils.ip.IPUtils;
import xingu.cloud.spawner.impl.SurrogateSupport;

public class GCloudSurrogate
	extends SurrogateSupport
{
	private String	zone;

	public GCloudSurrogate(String id, String address, String zone)
	{
		super(id);
		this.ip = IPUtils.from(address);
		this.zone = zone;
	}

	public String getZone()
	{
		return zone;
	}
}