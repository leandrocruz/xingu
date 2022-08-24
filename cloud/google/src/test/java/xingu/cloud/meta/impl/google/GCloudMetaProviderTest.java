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
		File body    = getFile("curl-get-instance-id.body");
		File headers = getFile("curl-get-instance-id.headers");
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/instance/name").to(body, headers);
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/instance/attributes/server")	.to(/* bogus */ body, headers);
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/instance/attributes/ports")	.to(/* bogus */ body, headers);
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/instance/zone")				.to(/* bogus */ body, headers);
		new HttpMocker(http).get(GCloudMetaProvider.BASE_URL + "/project/project-id")			.to(/* bogus */ body, headers);
	}

	@Test
	public void testGetInstanceId()
		throws Exception
	{
		String instanceId = meta.getInstanceId();
		assertEquals("oystr-1234-sample", instanceId);
	}
}
