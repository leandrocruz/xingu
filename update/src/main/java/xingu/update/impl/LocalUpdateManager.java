package xingu.update.impl;

import java.io.InputStream;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;

public class LocalUpdateManager
	extends UpdateManagerSupport
{
	@Override
	protected BundleDescriptors getRemoteDescriptors()
		throws Exception
	{
		return new BundleDescriptorsImpl();
	}

	@Override
	protected InputStream toInputStream(BundleDescriptor desc)
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
