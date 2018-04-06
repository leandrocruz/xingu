package xingu.cloud.meta.impl.google;

import org.apache.avalon.framework.activity.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.cloud.meta.MetaProvider;
import xingu.container.Inject;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpResponse;
import xingu.utils.StringUtils;

public class GCloudMetaProvider
	implements MetaProvider, Initializable
{
	@Inject("gcloud")
	private HttpClient			http;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String	BASE_URL = "http://metadata.google.internal/computeMetadata/v1/instance";

	private String hostname;

	private String server;

	private String ports;
	
	@Override
	public void initialize()
		throws Exception
	{
		this.hostname = envOr("OYSTR_HOSTNAME", "/hostname");
		this.server   = envOr("OYSTR_SERVER", 	"/attributes/server");
		this.ports    = envOr("OYSTR_PORTS", 	"/attributes/ports");
	}

	@Override
	public String getInstanceId()
		throws Exception
	{
		String host = StringUtils.trim(hostname);
		int    idx  = host.indexOf(".");
		return host.substring(0, idx);
	}

	@Override
	public String getHost()
		throws Exception
	{
		return StringUtils.trim(server);
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		String[] array = ports.split("\\|");
		int size = array.length;
		int[] result = new int[size];
		for(int i = 0; i < size; i++)
		{
			String value = array[i].trim();
			result[i] = StringUtils.toInt(value, 0);
		}
		return result;
	}

	private String envOr(String env, String meta)
		throws Exception
	{
		String result = System.getenv(env);
		if(result == null)
		{
			result = get(meta);
			logger.info("{} = '{}'", meta, result);
		}
		else
		{
			logger.info("{} = '{}'", env, result);
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