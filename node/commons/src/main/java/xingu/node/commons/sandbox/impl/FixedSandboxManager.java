package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.util.Iterator;

import xingu.container.Inject;
import xingu.http.client.HttpProgressListener;
import xingu.http.client.impl.StepListener;
import xingu.lang.NotImplementedYet;
import xingu.node.commons.sandbox.SandboxDescriptor;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;
import xingu.update.UpdateManager;
import xingu.utils.StringUtils;

public class FixedSandboxManager
	extends SandboxManagerSupport
{
	@Inject
	private UpdateManager updater;

	class LogProgressListener
		extends StepListener
	{
		long lastCall = -1;
		long lastBytes = 0;

		public LogProgressListener(int step)
		{
			super(step);
		}

		@Override
		protected void log(long total, long progress)
		{
			long   now    = System.currentTimeMillis();
			long   millis = lastCall < 0 ? -1 : now - lastCall;
			long   bytes  = progress - lastBytes;
			double rate   = millis < 0 ? -1 : ((double) bytes * 1000) / (double) millis;

			lastBytes = progress;
			lastCall  = now;
			
			logger.info("Progress {} - {}/sec", StringUtils.toPercent(progress, total), toNumInUnits((long)rate));
		}
		
		public String toNumInUnits(long bytes)
		{
			int u = 0;
		    int threshold = 1024*1024;
			for (;bytes > threshold; bytes >>= 10)
		    {
		        u++;
		    }
		    if (bytes > 1024)
		    {
		    	u++;
		    }
		    return String.format("%.1f %cB", bytes/1024f, " kMGTPE".charAt(u));
		}
	}

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
			updater.update(id, new LogProgressListener(5 /* log every 5% */));
		}
	}

	@Override
	protected SandboxDescriptor load(String id)
		throws Exception
	{
		throw new NotImplementedYet("RemoteSandboxManager can't load sandboxes on the fly");
	}
}