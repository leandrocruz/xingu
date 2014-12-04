package xingu.cloud.spawner.impl.google;

import br.com.ibnetwork.xingu.utils.ip.IPUtils;
import xingu.cloud.spawner.impl.SurrogateSupport;

public class GCloudSurrogate
	extends SurrogateSupport
{
	public GCloudSurrogate(String id, String address, String zone)
	{
		super(id, zone);
		this.ip = IPUtils.from(address);
	}
}