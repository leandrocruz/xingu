package xingu.cloud.meta.impl.local;

import java.io.File;
import java.io.IOException;

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
		File file = new File(root, "instance-ids");
		if(!file.exists())
		{
			return "local";
		}
			
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
		return readFileToStringOr(new File(root, "host"), "localhost");
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		String result = readFileToStringOr(new File(root, "port"), "9556");
		result = StringUtils.trim(result);
		int port = Integer.parseInt(result);
		return new int[]{port};
	}

	private String readFileToStringOr(File file, String deftl)
		throws IOException
	{
		if(file.exists())
		{
			String result = FileUtils.readFileToString(file);
			return StringUtils.trim(result);			
		}
		else
		{
			return deftl;
		}		
	}
}
