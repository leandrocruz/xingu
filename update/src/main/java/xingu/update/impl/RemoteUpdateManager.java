package xingu.update.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.time.Time;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class RemoteUpdateManager
	implements UpdateManager, Configurable, Initializable
{
	@Inject("update-manager")
	protected HttpClient		http;

	@Inject
	protected Time				time;

	protected File				local;

	protected String			remote;

	private String				bundlesFile;

	private BundleDescriptors	localDescriptors;

	private BundleDescriptors	remoteDescriptors;
	
	private static final DateFormat	format	= new SimpleDateFormat("yyyMMdd.HHmmss");

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
			String hash    = c.getAttribute("hash");
			list.add(new BundleDescriptorImpl(id, version, file, hash));
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
			String v1 = remote.getVersion();
			String v2 = local.getVersion();
			if(v1.equals(v2))
			{
				return findLocalFile(remote).exists();
			}
			else
			{
				return true;
			}
		}
	}

	private File findLocalFile(BundleDescriptor descriptor)
	{
		String name = descriptor.getId() + File.separator + descriptor.getFile();
		return new File(local, name);
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

		File target = findLocalFile(remote);
		if(target.exists())
		{
			throw new NotImplementedYet("Can't override local file: " + target);	
		}

		InputStream is   = getRemote(remote);
		byte[]      data = IOUtils.toByteArray(is);
		FileUtils.writeByteArrayToFile(target, data, false);
		
		localDescriptors.put(remote);
		writeConfig();

		return remote;
	}

	private void writeConfig()
		throws IOException
	{
		Date   date    = time.now().asDate();
		String version = format.format(date);
		
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<repo version=\"")
			.append(version)
			.append("\">\n\t<bundles>\n");

		Iterator<BundleDescriptor> it = localDescriptors.iterator();
		while(it.hasNext())
		{
			BundleDescriptor desc = it.next();
			sb
				.append("\t\t<bundle id=\"").append(desc.getId()).append("\"\n")
				.append("\t\t\tversion=\"").append(desc.getVersion()).append("\"\n")
				.append("\t\t\tfile=\"").append(desc.getFile()).append("\"\n")
				.append("\t\t\thash=\"").append(desc.getHash()).append("\" />\n");
		}
		
		sb.append("\t</bundles>\n</repo>");
		
		String config = sb.toString();
		File file = new File(local, bundlesFile);
		FileUtils.write(file, config);
	}

	private InputStream getRemote(BundleDescriptor remote)
	{
		String       file = remote.getFile();
		String       id   = remote.getId();
		String       uri  = this.remote + "/" + id + "/" + file;
		HttpResponse res  = http.get(uri).exec();
		InputStream  is   = res.getRawBody();
		return is;
	}
}
