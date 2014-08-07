package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.util.Iterator;

import xingu.node.commons.sandbox.SandboxDescriptor;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class FixedSandboxManager
	extends SandboxManagerSupport
{
	@Inject
	private UpdateManager updater;

	@Override
	protected SandboxDescriptor[] getSandboxDescriptors()
		throws Exception
	{
		doUpdate();
		
		BundleDescriptors   bundles = updater.getBundles();
		SandboxDescriptor[] result  = new SandboxDescriptor[bundles.size()];
		
		Iterator<BundleDescriptor> it = bundles.iterator();
		int i = 0;
		while(it.hasNext())
		{
			BundleDescriptor bundle = it.next();
			String id = bundle.getId();
			File file = updater.getAbsoluteFile(bundle);
			result[i++] = new SandboxDescriptorImpl(id, file);
		}

		return result;
	}

	private void doUpdate()
		throws Exception
	{
		BundleDescriptors updates = updater.getUpdates();
		Iterator<BundleDescriptor> it = updates.iterator();
		while(it.hasNext())
		{
			BundleDescriptor desc = it.next();
			String id = desc.getId();
			updater.update(id);
		}
	}

	@Override
	protected SandboxDescriptor load(String id)
		throws Exception
	{
		throw new NotImplementedYet("RemoteSandboxManager can't load sandboxes on the fly");
	}
}