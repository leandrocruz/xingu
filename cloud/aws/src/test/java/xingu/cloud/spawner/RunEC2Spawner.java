package xingu.cloud.spawner;

import java.util.List;

import org.apache.avalon.framework.configuration.DefaultConfiguration;

import xingu.cloud.spawner.impl.ec2.EC2Spawner;
import xingu.utils.ip.IPAddress;

public class RunEC2Spawner
{
	public static void main(String[] args)
		throws Exception
	{
		EC2Spawner           spawner = new EC2Spawner();
		DefaultConfiguration conf    = new DefaultConfiguration("component");
		DefaultConfiguration aws     = new DefaultConfiguration("aws");
		aws.setAttribute("secrets"		, "/opt/rudi/server/AwsCredentials.properties");
		aws.setAttribute("endpoint"		, "ec2.sa-east-1.amazonaws.com");
		aws.setAttribute("keyName"		, "oystr");
		aws.setAttribute("securityGroup", "oystr");
		aws.setAttribute("instanceType"	, "t1.micro");
		aws.setAttribute("imageId"		, "ami-85a20498");
		conf.addChild(aws);
		
		spawner.configure(conf);
		spawner.start();
		SpawnRequest req = SpawnRequestFactory.builder().get(1);
		List<Surrogate> surrogates = spawner.spawn(req);
		
		for(Surrogate surrogate : surrogates)
		{
			String    id   = surrogate.getId();
			IPAddress addr = surrogate.getIp();
			System.out.println(id + " at " + addr);
		}
	}
}