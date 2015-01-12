package xingu.cloud.meta.impl.google;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import xingu.cloud.meta.MetaProvider;
import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.http.client.HttpClient;
import xingu.http.client.mock.HttpMocker;

public class GCloudMetaProviderTest
	extends XinguTestCase
{
	@Inject
	private MetaProvider meta;

	@Inject("gcloud")
	private HttpClient http;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(MetaProvider.class).to(GCloudMetaProvider.class);
		withMock("gcloud", HttpClient.class);
	}

	@Test
	public void testGetInstanceId()
		throws Exception
	{
		File file = getFile("curl-get-instance-id.html");
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/hostname").to(file);
		String instanceId = meta.getInstanceId();
		assertEquals("oystr-1234-sample", instanceId);
	}
}
