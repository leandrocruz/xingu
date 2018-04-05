package xingu.cloud.meta.impl.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.cloud.meta.MetaProvider;
import xingu.container.Inject;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpResponse;
import xingu.utils.StringUtils;

public class GCloudMetaProvider
	implements MetaProvider
{
	@Inject("gcloud")
	private HttpClient			http;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String	BASE_URL = "http://metadata.google.internal/computeMetadata/v1/instance";

	@Override
	public String getInstanceId()
		throws Exception
	{
		String body = get("/hostname");
		logger.info("/hostname: {}", body);
		String host = StringUtils.trim(body);
		int    idx  = host.indexOf(".");
		return host.substring(0, idx);
	}

	@Override
	public String getHost()
		throws Exception
	{
		String body = get("/attributes/server");
		logger.info("/attributes/server: {}", body);
		return StringUtils.trim(body);
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		String body = get("/attributes/ports");
		logger.info("/attributes/ports: {}", body);
		return readPorts(body);
	}

	private int[] readPorts(String in)
	{
		String[] ports = in.split("\\|");
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
		if(res.getCode() != 200)
		{
			throw new Exception("Call to '"+url+"' returned "+res.getCode());
		}
		return res.getBody();
	}
}