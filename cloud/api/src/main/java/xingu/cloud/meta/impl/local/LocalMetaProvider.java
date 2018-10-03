package xingu.cloud.meta.impl.local;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import xingu.cloud.meta.MetaProvider;

public class LocalMetaProvider
	implements MetaProvider
{
	private File root = new File("/tmp/oystr/meta");

	private String extractValueAndUpdateFile(String dftl, File file)
		throws Exception
	{
		String str = readFileToStringOr(file, dftl);
		List<String> values = Arrays.asList(str.split("\n"));
		int index = values.size() - 1;
		List<String> remaining = values.subList(0, index);
		String value = values.get(index);
		FileUtils.write(file, StringUtils.join(remaining, "\n") + "\n");
		return StringUtils.trim(value);		
	}
	
	@Override
	public String getInstanceId()
		throws Exception
	{
		return extractValueAndUpdateFile("local", new File(root, "instance-ids"));
	}

	@Override
	public String getHost()
		throws Exception
	{
		return extractValueAndUpdateFile("localhost", new File(root, "host"));
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		String port = extractValueAndUpdateFile("9556", new File(root, "port"));
		return new int[]{Integer.parseInt(port)};
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
