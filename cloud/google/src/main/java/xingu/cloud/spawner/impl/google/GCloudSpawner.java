package xingu.cloud.spawner.impl.google;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;
import xingu.cloud.spawner.impl.SpawnerSupport;
import xingu.cloud.spawner.impl.google.model.AccessConfig;
import xingu.cloud.spawner.impl.google.model.CreateJsonReply;
import xingu.cloud.spawner.impl.google.model.NetworkInterface;
import xingu.container.Inject;
import xingu.lang.NotImplementedYet;
import xingu.process.ProcessManager;
import xingu.utils.NameValue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GCloudSpawner
	extends SpawnerSupport
	implements Spawner, Configurable, Initializable
{
	private final static String PARAM_SEPARATOR = "x-x-x";
	
	@Inject
	private ProcessManager	pm;

	private String			bin;

	private ObjectMapper	mapper;

	private final Logger	logger	= LoggerFactory.getLogger(getClass());

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		bin = conf.getChild("gcloud").getAttribute("path", "/usr/local/bin/gcloud");
	}

	@Override
	public void initialize()
		throws Exception
	{
		mapper = new ObjectMapper();
	}

	/**
	 * See: https://cloud.google.com/sdk/gcloud/reference/compute/instances/delete
	 */
	
	@Override
	protected void stopSurrogate(Surrogate surrogate)
		throws Exception
	{
		String id   = surrogate.getId();
		String zone = surrogate.getRegion();
		
		List<String> cmd = new ArrayList<String>();
		cmd.add(bin);
		cmd.add("compute");
		cmd.add("instances");
		cmd.add("delete");
		cmd.add(id);
		cmd.add("--zone");
		cmd.add(zone);
		cmd.add("--quiet");

		String result = execute(cmd);
		parseRelease(result);
	}
	
	private void parseRelease(String result)
	{}

	@Override
	protected void startSurrogate(SpawnRequest req, List<Surrogate> surrogates)
		throws Exception
	{
		String zone        = req.getRegion();
		String project     = req.getNamespace();
		String machineType = req.getMachineType();
		String image       = req.getImage();
		
		List<String> cmd = new ArrayList<String>();
		cmd.add(bin);
		cmd.add("compute");
		cmd.add("instances");
		cmd.add("create");
		for(Surrogate surrogate : surrogates)
		{
			String id = surrogate.getId();
			cmd.add(id);
		}
		cmd.add("--zone");
		cmd.add(zone);
		cmd.add("--project");
		cmd.add(project);
		cmd.add("--machine-type");
		cmd.add(machineType);
		cmd.add("--image");
		cmd.add(image);
		cmd.add("--format");
		cmd.add("json");
		
		List<NameValue<String>> meta = req.getMeta();
		if(meta.size() > 0)
		{
			cmd.add("--metadata");
			StringBuilder metaDataBuffer = new StringBuilder();
			metaDataBuffer.append("^").append(PARAM_SEPARATOR).append("^");
			for(NameValue<String> item : meta)
			{
				metaDataBuffer.append(item.name + "=" + item.value);
				metaDataBuffer.append(PARAM_SEPARATOR);
			}
			cmd.add(metaDataBuffer.toString());
		}

		String result = execute(cmd);
		//parseSpawn(result);
	}

	private List<Surrogate> parseSpawn(String in)
		throws Exception
	{
		List<CreateJsonReply> result = mapper.readValue(in, new TypeReference<List<CreateJsonReply>>(){});
		List<Surrogate> surrogates = new ArrayList<>(result.size());
		for(CreateJsonReply reply : result)
		{
			Surrogate surrogate = toSurrogate(reply);
			surrogates.add(surrogate);
		}
		return surrogates;
	}

	private Surrogate toSurrogate(CreateJsonReply reply)
	{
		NetworkInterface iface0        = reply.getNetworkInterfaces().get(0);
		AccessConfig     accessConfig0 = iface0.getAccessConfigs().get(0);
		String address = accessConfig0.getNatIP();
		String id      = reply.getName();
		String zone    = reply.getZone();
		return new GCloudSurrogate(id, address, zone);
	}

	private String execute(List<String> cmd)
		throws Exception
	{
		logger.info("Executing command: {}", StringUtils.join(cmd, " "));
		File baseDir = org.apache.commons.io.FileUtils.getTempDirectory();
		File output  = File.createTempFile("gcloud-output-", ".txt");
		File error   = File.createTempFile("gcloud-error-", ".txt");
		int  result  = pm.exec(cmd, baseDir, output, error);
		if(result != 0)
		{
			String data = FileUtils.readFileToString(error);
			throw new NotImplementedYet("Error executing gcloud: " + data);
		}

		return FileUtils.readFileToString(output);
	}
}