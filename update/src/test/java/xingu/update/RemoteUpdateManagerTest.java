package xingu.update;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Test;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.update.impl.RemoteUpdateManager;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;


public class RemoteUpdateManagerTest
	extends XinguTestCase
{
	@Inject("update-manager")
	private HttpClient	http;
	
	@Inject
	private UpdateManager updater;

	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		File         repo     = getFile("repo");
		InputStream  is       = new FileInputStream(new File(repo, "remote.xml"));
		HttpResponse response = mock(HttpResponse.class);
		HttpRequest  request  = mock(HttpRequest.class);
		HttpClient   http     = mock(HttpClient.class);
		when(response.getRawBody()).thenReturn(is);
		when(http.get("http://repo.com/repo/descriptor.xml")).thenReturn(request);
		when(request.exec()).thenReturn(response);

		Configuration conf = this.buildFrom("<x><repo remote=\"http://repo.com/repo\" local=\""+repo+"\"/></x>");
		binder.bind(UpdateManager.class).to(RemoteUpdateManager.class).with(conf);
		binder.bind(HttpClient.class, "update-manager").to(http);
	}
	
	@Test
	public void testUpdates()
		throws Exception
	{
		BundleDescriptors updates = updater.getUpdates();
		assertEquals(2, updates.size());
	}
}
