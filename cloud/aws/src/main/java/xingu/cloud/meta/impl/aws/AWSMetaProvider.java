package xingu.cloud.meta.impl.aws;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.StringUtils;
import xingu.cloud.meta.MetaProvider;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpResponse;

public class AWSMetaProvider
	implements MetaProvider
{
	@Inject("aws")
	private HttpClient			http;

	@Override
	public String getInstanceId()
		throws Exception
	{
		/*
		 * EC2 Metadata
		 * http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AESDG-
		 * chapter-instancedata.html
		 * http://docs.aws.amazon.com/AWSEC2/2007-03-01
		 * /DeveloperGuide/AESDG-chapter-instancedata.html
		 */
		HttpResponse res = http.get("http://169.254.169.254/latest/meta-data/instance-id").exec();
		String body = res.getBody();
		return StringUtils.trim(body);

	}

	@Override
	public String getHost()
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public int[] getPorts()
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
