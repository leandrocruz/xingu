package xingu.cloud.meta.impl.local;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import xingu.cloud.meta.MetaProvider;
import xingu.lang.NotImplementedYet;

public class LocalMetaProvider
	implements MetaProvider
{
	private File root = new File("/tmp/oystr/meta");
	
	@Override
	public String getInstanceId()
		throws Exception
	{
		File     file  = new File(root, "instance-ids");
		String   id    = StringUtils.trim(FileUtils.readFileToString(file));
		String[] parts = id.split(",");
		int      len   = parts.length;
		if(len <= 0)
		{
			throw new NotImplementedYet();
		}
		
		id = parts[0];
		if(len > 1)
		{
			//we have more ids
			String[] remaining = new String[len - 1];
			System.arraycopy(parts, 1, remaining, 0, len - 1);
			FileUtils.write(file, StringUtils.join(remaining, ","));
		}
		else
		{
			FileUtils.write(file, "");
		}
		return id;
	}

	@Override
	public String getHost()
		throws Exception
	{
		String result = FileUtils.readFileToString(new File(root, "host"));
		return StringUtils.trim(result);
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		String result = FileUtils.readFileToString(new File(root, "port"));
		result = StringUtils.trim(result);
		int port = Integer.parseInt(result);
		return new int[]{port};
	}
}
