package xingu.cloud.vm.impl.google;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class GCloudSpawner
	extends SpawnerSupport
	implements Spawner, Configurable
{
	@Inject
	private ProcessManager	pm;

	private String			bin;

	private final Logger	logger	= LoggerFactory.getLogger(getClass());

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		bin = conf.getChild("gcloud").getAttribute("path", "/usr/local/bin/gcloud");
	}
	
	@Override
	protected Surrogate spawn(String id, String name, SpawnRequest req)
		throws Exception
	{
		List<String> cmd = buildLine(id, name, req);
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

		String data = FileUtils.readFileToString(output);
		return parse(name, data);
	}

	private Surrogate parse(String nameToMatch, String data)
	{
		String[] lines = data.split("\n");
		for(String line : lines)
		{
			if(line.startsWith(nameToMatch))
			{
				String[] parts      = StringUtils.split(line);
				String   name       = parts[0];
				String   externalIp = parts[4];
				return new GCloudSurrogate(name, externalIp);
			}
		}
		throw new NotImplementedYet("Error parsing gcloud output for '"+nameToMatch+"' : " + data);
	}

	private List<String> buildLine(String id, String name, SpawnRequest req)
	{
		String zone        = req.getZone();
		String project     = req.getProject();
		String machineType = req.getMachineType();
		String image       = req.getImage();
		
		List<String> params = new ArrayList<String>();
		params.add(bin);
		params.add("compute");
		params.add("instances");
		params.add("create");
		params.add(name);
		params.add("--zone");
		params.add(zone);
		params.add("--project");
		params.add(project);
		params.add("--machine-type");
		params.add(machineType);
		params.add("--image");
		params.add(image);
		params.add("--metadata");
		params.add("instanceId=" + id);
		 
		return params;
	}
}