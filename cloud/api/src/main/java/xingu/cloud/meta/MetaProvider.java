package xingu.cloud.meta;

public interface MetaProvider
{
	String getInstanceId()
		throws Exception;

	String getHost()
		throws Exception;

	int[] getPorts()
		throws Exception;
}
