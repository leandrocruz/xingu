package xingu.cloud.meta.impl.local;

import java.io.File;

import org.apache.commons.io.FileUtils;

import br.com.ibnetwork.xingu.utils.StringUtils;
import xingu.cloud.meta.MetaProvider;

public class LocalMetaProvider
	implements MetaProvider
{
	@Override
	public String getInstanceId()
		throws Exception
	{
		File file = new File("/tmp/oystr/meta");
		String id = FileUtils.readFileToString(file);
		return StringUtils.trim(id);
	}

	@Override
	public String getHost()
		throws Exception
	{
		return null;
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		return null;
	}
}
