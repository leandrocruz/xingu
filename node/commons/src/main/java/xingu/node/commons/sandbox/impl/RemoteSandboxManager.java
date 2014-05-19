package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;

import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxDescriptor;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.io.zip.ZipUtils;

public class RemoteSandboxManager
	extends SandboxManagerSupport
{
	@Inject
	private UpdateManager updater;

	@Override
	protected Sandbox load(String id)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	protected SandboxDescriptor[] getSandboxDescriptors()
		throws Exception
	{
		checkForUpdates();
		
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
	
	private void checkForUpdates()
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
	protected File sourceDirectoryFor(SandboxDescriptor desc)
		throws Exception
	{
		File   file    = desc.getFile();
		String name    = file.getName();
		String dirName = FilenameUtils.getBaseName(name);
		File   dir     = new File(file.getParentFile(), dirName);
		if(!dir.exists())
		{
			ZipUtils.explode(file, dir);
		}
		return dir;
	}
}