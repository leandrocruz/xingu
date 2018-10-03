package xingu.cloud.meta.impl.google;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
		this.http = mock(HttpClient.class);
		binder.bind(HttpClient.class, "gcloud").to(this.http);
		File file = getFile("curl-get-instance-id.html");
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/attributes/instanceId").to(file);
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/attributes/server").to(/* bogus */ file);
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/attributes/ports").to( /* bogus */ file);
	}

	@Test
	public void testGetInstanceId()
		throws Exception
	{
		String instanceId = meta.getInstanceId();
		assertEquals("oystr-1234-sample", instanceId);
	}
}
