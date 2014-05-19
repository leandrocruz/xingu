package xingu.node.commons.sandbox.impl;

import xingu.node.commons.sandbox.Sandbox;
import xingu.update.BundleDescriptor;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class RemoteSandboxManager
	extends SandboxManagerSupport
{
	@Inject
	private UpdateManager updater;

	@Override
	protected Sandbox load(String id)
		throws Exception
	{
		updater.getUpdates();
		BundleDescriptor bundle = updater.update(id);
		return toSandbox(bundle);
	}

	private Sandbox toSandbox(BundleDescriptor bundle)
	{
		throw new NotImplementedYet();
	}
}