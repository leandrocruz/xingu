package xingu.cloud.spawner.impl.ec2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;
import xingu.cloud.spawner.impl.SpawnerSupport;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

public class EC2Spawner
	extends SpawnerSupport
	implements Spawner, Configurable, Startable
{
	private AmazonEC2Client	ec2;

	private String			secrets;

	private String			endpoint; /* see: http://docs.aws.amazon.com/general/latest/gr/rande.html#ec2_region */

	private String			keyName;

	private String			securityGroup;

	private String			instanceType;

	private String			imageId;
	
	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		conf          = conf.getChild("aws");
		secrets       = conf.getAttribute("secrets");
		endpoint      = conf.getAttribute("endpoint");
		keyName       = conf.getAttribute("keyName");
		securityGroup = conf.getAttribute("securityGroup");
		instanceType  = conf.getAttribute("instanceType");
		imageId       = conf.getAttribute("imageId");
	}

	@Override
	public void start()
		throws Exception
	{
		InputStream    in            = new FileInputStream(new File(secrets));
		AWSCredentials credentials   = new PropertiesCredentials(in);
		IOUtils.closeQuietly(in);
		ec2 = new AmazonEC2Client(credentials);
		ec2.setEndpoint(endpoint);
	}

	@Override
	public void stop()
		throws Exception
	{}

	@Override
	public synchronized void release(Surrogate surrogate)
	{
		String id = surrogate.getId();
		logger.info("Releasing Surrogate s#{}", id);
	}

	@Override
	public List<Surrogate> spawn(SpawnRequest reqX)
		throws Exception
	{
		int count = reqX.getCount();
		String meta = "TODO";
		
		RunInstancesRequest req = new RunInstancesRequest();
		req.withImageId(imageId)
			.withInstanceType(instanceType)
			.withMinCount(count)
			.withMaxCount(count)
			.withKeyName(keyName)
			.withSecurityGroups(securityGroup);
		
		if(StringUtils.isNotEmpty(meta))
		{
			String data = encode(meta);
			req.withUserData(data);
		}
			
		RunInstancesResult result      = ec2.runInstances(req);
		Reservation        reservation = result.getReservation();
		List<Instance>     instances   = reservation.getInstances();
		List<Surrogate>    surrogates  = new ArrayList<Surrogate>(instances.size());
		for(Instance instance : instances)
		{
			EC2Surrogate surrogate = new EC2Surrogate(instance);
			String       id        = surrogate.getId();
			logger.info("Creating surrogate #{} from image '{}'", id, imageId);
			surrogateById.put(id, surrogate);
			surrogates.add(surrogate);
		}
		
		return surrogates;
	}

	private String encode(String userData)
	{
		byte[] data = Base64.encodeBase64(userData.getBytes());
		return new String(data);
	}

	@Override
	protected Surrogate spawn(String id, SpawnRequest req)
		throws Exception
	{
		throw new NotImplementedYet();
	}
}