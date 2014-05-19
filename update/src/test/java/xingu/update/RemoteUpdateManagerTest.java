package xingu.update;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.Iterator;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Test;

import xingu.http.client.HttpClient;
import xingu.http.client.mock.HttpMocker;
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
	protected boolean resetContainer()
	{
		return true;
	}

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		HttpClient   http = mock(HttpClient.class);
		File         repo = getFile("repo");
		
		new HttpMocker(http)
			.get("http://repo.com/repo/bundles.xml")
			.to(new File(repo, "remote.xml"));
		
		new HttpMocker(http)
			.get("http://repo.com/repo/bundleA/bundleA.zip")
			.to(new File(repo, "bundleA.zip"));

		new HttpMocker(http)
			.get("http://repo.com/repo/bundleC/bundleC-1.0.zip")
			.to(new File(repo, "bundleC-1.0.zip"));

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
		Iterator<BundleDescriptor> it = updates.iterator();
		while(it.hasNext())
		{
			BundleDescriptor descriptor = it.next();
			String id = descriptor.getId();
			updater.update(id);
		}
	}
}
