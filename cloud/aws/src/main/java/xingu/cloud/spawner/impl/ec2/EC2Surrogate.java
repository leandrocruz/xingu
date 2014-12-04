package xingu.cloud.spawner.impl.ec2;

import xingu.cloud.spawner.impl.SurrogateSupport;

import com.amazonaws.services.ec2.model.Instance;

public class EC2Surrogate
	extends SurrogateSupport
{
	private final Instance instance;

	public EC2Surrogate(Instance instance, String region)
	{
		super(instance.getInstanceId(), region);
		this.instance = instance;
	}
}