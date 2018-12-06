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

	public static final String	BASE_URL = "http://metadata.google.internal/computeMetadata/v1";

	private String instanceId;

	private String server;

	private String ports;

	private String project;

	private String zone;
	
	@Override
	public void initialize()
		throws Exception
	{
		this.project	= envOr("OYSTR_PROJECT"		, "/project/project-id");
		this.zone		= envOr("OYSTR_ZONE"		, "/instance/zone");
		this.instanceId = envOr("OYSTR_INSTANCE_ID" , "/instance/name");
		this.server   	= envOr("OYSTR_SERVER"	 	, "/instance/attributes/server");
		this.ports    	= envOr("OYSTR_PORTS"		, "/instance/attributes/ports");
	}

	@Override
	public String getProject()
	{
		return project;
	}

	@Override
	public String getZone()
	{
		/* See:
		 * - https://cloud.google.com/compute/docs/storing-retrieving-metadata
		 * 
		 * "projects/[NUMERIC_PROJECT_ID]/zones/[ZONE]"
		 */
		return zone.substring(zone.lastIndexOf("/") + 1);
	}

	@Override
	public String getInstanceId()
		throws Exception
	{
		return instanceId;
	}

	@Override
	public String getHost()
		throws Exception
	{
		return server;
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		return getPorts(this.ports);
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
		return StringUtils.trim(result);
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