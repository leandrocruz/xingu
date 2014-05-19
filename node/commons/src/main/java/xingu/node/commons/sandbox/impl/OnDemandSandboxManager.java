package xingu.node.commons.sandbox.impl;

import java.io.File;

import xingu.node.commons.sandbox.SandboxDescriptor;
import xingu.update.BundleDescriptor;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;

public class OnDemandSandboxManager
	extends SandboxManagerSupport
{
	@Inject
	private UpdateManager updater;

	@Override
	protected SandboxDescriptor[] getSandboxDescriptors()
		throws Exception
	{
		return null;
	}

	@Override
	protected SandboxDescriptor load(String id)
		throws Exception
	{
		BundleDescriptor bundle = updater.update(id);
		File file = updater.getAbsoluteFile(bundle);
		return new SandboxDescriptorImpl(id, file);
	}
}