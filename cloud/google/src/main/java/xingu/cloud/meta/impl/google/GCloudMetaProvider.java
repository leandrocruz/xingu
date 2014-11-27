package xingu.cloud.meta.impl.google;

import xingu.cloud.meta.MetaProvider;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpResponse;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class GCloudMetaProvider
	implements MetaProvider
{
	@Inject("gcloud")
	private HttpClient			http;

	public static final String	BASE_URL	= "http://metadata.google.internal/computeMetadata/v1/instance";

	@Override
	public String getInstanceId()
		throws Exception
	{
		String body = get("/hostname");
		String host = StringUtils.trim(body);
		int    idx  = host.indexOf(".");
		return host.substring(0, idx);
	}

	@Override
	public String getHost()
		throws Exception
	{
		String body = get("/attributes/host");
		return StringUtils.trim(body);
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		String body = get("/attributes/ports");
		return readPorts(body);
	}

	private int[] readPorts(String in)
	{
		String[] ports = in.split(",");
		int size = ports.length;
		int[] result = new int[size];
		for(int i = 0; i < size; i++)
		{
			String value = ports[i].trim();
			result[i] = StringUtils.toInt(value, 0);
		}
		return result;
	}
	
	private String get(String url)
		throws Exception
	{
		HttpResponse res = http.get(BASE_URL + url).header("Metadata-Flavor", "Google").exec();
		return res.getBody();
	}
}