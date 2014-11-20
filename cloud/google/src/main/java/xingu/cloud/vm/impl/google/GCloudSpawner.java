package xingu.cloud.vm.impl.google;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.cloud.vm.SpawnRequest;
import xingu.cloud.vm.Spawner;
import xingu.cloud.vm.VirtualMachine;
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class GCloudSpawner
	implements Spawner
{
	@Inject
	private ProcessManager	pm;

	private String			bin		= "/home/leandro/bin/gcloud";

	private final Logger	logger	= LoggerFactory.getLogger(getClass());
	
	@Override
	public List<VirtualMachine> spawn(SpawnRequest req)
		throws Exception
	{
		List<String> cmd  = buildLine(req);
		logger.info("Executing command: {}", StringUtils.join(cmd, " "));
		File baseDir = org.apache.commons.io.FileUtils.getTempDirectory();
		File output  = File.createTempFile("gcloud-output-", ".txt");
		File error   = File.createTempFile("gcloud-error-", ".txt");
		int  result  = pm.exec(cmd, baseDir, output, error);
		
		if(result != 0)
		{
			throw new NotImplementedYet("Error executing gcloud");
		}
		
		String outputData = FileUtils.readFileToString(output);
		return parse(outputData);
	}

	private List<VirtualMachine> parse(String data)
	{
		return null;
	}

	private List<String> buildLine(SpawnRequest req)
	{
		String zone        = req.getZone();
		String project     = req.getProject();
		String group       = req.getGroup();
		String name        = req.getName();
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
		 
		return params;
	}
}
