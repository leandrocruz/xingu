package xingu.update.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.IOUtils;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;

public class RemoteUpdateManager
	implements UpdateManager, Configurable, Initializable
{
	@Inject("update-manager")
	protected HttpClient		http;

	protected File				local;

	protected String			remote;

	private String				bundlesFile;

	private BundleDescriptors	localDescriptors;

	private BundleDescriptors	remoteDescriptors;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		conf          = conf.getChild("repo");
		String desc   = conf.getAttribute("bundles", "bundles.xml");
		String remote = conf.getAttribute("remote");
		String local  = conf.getAttribute("local");
		this.local       = new File(local);
		this.remote      = remote;
		this.bundlesFile = desc;
	}

	@Override
	public void initialize()
		throws Exception
	{
		localDescriptors  = getLocalDescriptors();		
		remoteDescriptors = getRemoteDescriptors();
	}

//	private BundleDescriptor merge(BundleDescriptor remote, BundleDescriptor local)
//	{
//		String id      = remote.getId();
//		String version = local.getCurrentVersion();
//		String file    = local.getFile();
//		String hash    = local.getHash();
//		String last    = remote.getCurrentVersion();
//
//		BundleDescriptor bundle = new BundleDescriptorImpl(id, version, file, hash);
//		bundle.setLastVersion(last);
//		return bundle;
//	}

	private BundleDescriptors getLocalDescriptors()
		throws Exception
	{
		File file = new File(local, bundlesFile);
		if(!file.exists())
		{
			return new BundleDescriptorsImpl();
		}
		InputStream is = new FileInputStream(file);
		return parse(is);
	}

	private BundleDescriptors getRemoteDescriptors()
		throws Exception
	{
		String       uri      = remote + "/" + bundlesFile;
		HttpRequest  req      = http.get(uri);
		HttpResponse response = req.exec();
		InputStream  is       = response.getRawBody();
		return parse(is);
	}		

	private BundleDescriptors parse(InputStream is)
		throws Exception
	{
		List<BundleDescriptor> list = new ArrayList<BundleDescriptor>();
		Configuration          conf = new DefaultConfigurationBuilder().build(is);
		IOUtils.closeQuietly(is);

		Configuration[] bundles = conf.getChild("bundles").getChildren("bundle");
		for(Configuration c : bundles)
		{
			String id      = c.getAttribute("id");
			String file    = c.getAttribute("file");
			String version = c.getAttribute("version");
			list.add(new BundleDescriptorImpl(id, version, file, null /* hash */));
		}

		return new BundleDescriptorsImpl(list);
	}

	@Override
	public BundleDescriptors getUpdates()
		throws Exception
	{
		List<BundleDescriptor>     result  = new ArrayList<BundleDescriptor>();
		Iterator<BundleDescriptor> it      = remoteDescriptors.iterator();
		while(it.hasNext())
		{
			BundleDescriptor remote   = it.next();
			String           id       = remote.getId();
			BundleDescriptor local    = localDescriptors.byId(id);
			boolean          required = isUpdateRequired(remote, local);
			BundleDescriptor toAdd    = remote;
			if(required)
			{
				result.add(toAdd);
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
			//compare versions
			String v1 = remote.getCurrentVersion();
			String v2 = local.getCurrentVersion();
			if(v1.equals(v2))
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

	@Override
	public BundleDescriptor byId(String id)
	{
		return localDescriptors.byId(id);
	}

	@Override
	public BundleDescriptor update(String id)
		throws Exception
	{
		BundleDescriptor remote = remoteDescriptors.byId(id);
		BundleDescriptor local  = localDescriptors.byId(id);
		if(!isUpdateRequired(remote, local))
		{
			return local;
		}

		String       uri      = this.remote + "/" + id + "/" + remote.getFile();
		HttpResponse response = http.get(uri).exec();
		InputStream  is       = response.getRawBody();
		
		//TODO: save payload
		
		return null;
	}
}
