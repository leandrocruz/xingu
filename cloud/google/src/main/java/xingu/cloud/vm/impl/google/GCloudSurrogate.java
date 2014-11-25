package xingu.cloud.vm.impl.google;

import br.com.ibnetwork.xingu.utils.ip.IPUtils;
import xingu.cloud.spawner.impl.SurrogateSupport;

public class GCloudSurrogate
	extends SurrogateSupport
{
	public GCloudSurrogate(String id, String address)
	{
		super(id);
		this.ip = IPUtils.from(address);
	}
}
