package xingu.update.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.IOUtils;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpResponse;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;

public class RemoteUpdateManager
	implements UpdateManager, Configurable
{
	@Inject("update-manager")
	protected HttpClient		http;

	protected File				local;

	protected String			remote;

	protected String			containerFile;

	private String				descriptorFile;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		containerFile        = conf.getChild("container").getAttribute("file", "pulga.xml");
		conf                 = conf.getChild("repo");
		String        desc   = conf.getAttribute("descriptor", "descriptor.xml");
		String        remote = conf.getAttribute("remote");
		String        local  = conf.getAttribute("local");
		this.local          = new File(local);
		this.remote         = remote;
		this.descriptorFile = desc;
	}

	private BundleDescriptors mergeDescriptors()
		throws Exception
	{
		List<BundleDescriptor>     result  = new ArrayList<BundleDescriptor>();
		BundleDescriptors          remotes = dowloadDescriptors();
		BundleDescriptors          locals  = readDescriptors();
		Iterator<BundleDescriptor> it      = remotes.iterator();
		while(it.hasNext())
		{
			BundleDescriptor remote   = it.next();
			BundleDescriptor local    = locals.byId(remote.getId());
			boolean          required = isUpdateRequired(remote, local);
			if(required)
			{
				result.add(remote);
			}
		}
		return new BundleDescriptorsImpl(result);
	}

	private boolean isUpdateRequired(BundleDescriptor remote, BundleDescriptor local)
	{
		if(local == null)
		{
			//only on remote
			return true;
		}
		else
		{
			//compare version
			if(remote.getFile().equals(local.getFile()))
			{
				return findLocalFile(remote);
			}
			else
			{
				return true;
			}
		}
	}

	private boolean findLocalFile(BundleDescriptor descriptor)
	{
		return new File(local, descriptor.getFile()).exists();
	}

	private BundleDescriptors readDescriptors()
		throws Exception
	{
		File file = new File(local, descriptorFile);
		InputStream is = new FileInputStream(file);
		return parse(is);
	}

	private BundleDescriptors dowloadDescriptors()
		throws Exception
	{
		String       uri      = remote + "/" + descriptorFile;
		HttpResponse response = http.get(uri).exec();
		InputStream  is       = response.getRawBody();
		return parse(is);
	}

	private BundleDescriptors parse(InputStream is)
		throws Exception
	{
		List<BundleDescriptor> list = new ArrayList<BundleDescriptor>();
		Configuration          conf = new DefaultConfigurationBuilder().build(is);
		IOUtils.closeQuietly(is);

		Configuration[]        bundles = conf.getChild("bundles").getChildren("bundle");
		for(Configuration c : bundles)
		{
			String id   = c.getAttribute("id");
			String file = c.getAttribute("file");
			list.add(new BundleDescriptorImpl(id, file));
		}
		
		return new BundleDescriptorsImpl(list);
	}

	@Override
	public BundleDescriptors getUpdates()
		throws Exception
	{
		return mergeDescriptors();
	}
}
