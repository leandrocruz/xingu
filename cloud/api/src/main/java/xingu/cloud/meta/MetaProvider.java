package xingu.cloud.meta;

import xingu.utils.StringUtils;

public interface MetaProvider
{
	String getProject();

	String getZone();

	String getInstanceId()
		throws Exception;

	String getHost()
		throws Exception;

	int[] getPorts()
		throws Exception;
	
	default int[] getPorts(String str)
		throws Exception
	{
		String[] array = str.split("\\|");
		int size = array.length;
		int[] result = new int[size];
		for(int i = 0; i < size; i++)
		{
			String value = array[i].trim();
			result[i] = StringUtils.toInt(value, 0);
		}
		return result;
	}
}