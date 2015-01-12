package xingu.cloud.spawner.impl.google;

import xingu.cloud.spawner.impl.SurrogateSupport;
import xingu.utils.ip.IPUtils;

public class GCloudSurrogate
	extends SurrogateSupport
{
	public GCloudSurrogate(String id, String address, String zone)
	{
		super(id, zone);
		this.ip = IPUtils.from(address);
	}
}