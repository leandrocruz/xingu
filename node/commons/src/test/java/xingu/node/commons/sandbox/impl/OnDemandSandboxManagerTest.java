package xingu.node.commons.sandbox.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxManager;
import xingu.update.BundleDescriptor;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import static org.mockito.Mockito.*;

public class OnDemandSandboxManagerTest
	extends XinguTestCase
{
	@Inject
	private SandboxManager sandboxes;
	
	@Inject
	private UpdateManager updater;

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(SandboxManager.class).to(OnDemandSandboxManager.class);
		withMock(UpdateManager.class);
	}
	
	@Test
	public void testLoad()
		throws Exception
	{
		String id = "sandbox-1";
		File file = getFile(id + ".zip");
		BundleDescriptor bundle = mock(BundleDescriptor.class);
		when(updater.update(id)).thenReturn(bundle);
		when(updater.getAbsoluteFile(bundle)).thenReturn(file);
		
		Sandbox sandbox = sandboxes.byId(id);
		assertEquals(id, sandbox.id());

		NamedClassLoader cl = sandbox.classLoader();
		Class<?> clazz = cl.loadClass("org.apache.commons.lang.StringUtils");
		assertSame(clazz.getClassLoader(), cl);
	}
}
