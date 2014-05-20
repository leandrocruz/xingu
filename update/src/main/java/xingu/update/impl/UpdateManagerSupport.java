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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.time.Time;
import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;
import xingu.update.UpdateManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.MD5Utils;

public abstract class UpdateManagerSupport
	implements UpdateManager, Configurable, Initializable
{
	@Inject
	protected Time						time;

	protected File						local;

	protected String					bundlesFile;

	protected BundleDescriptors			localDescriptors;

	protected BundleDescriptors			remoteDescriptors;

	protected final Logger				logger	= LoggerFactory.getLogger(getClass());

	protected static final DateFormat	format	= new SimpleDateFormat("yyyMMdd.HHmmss");

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		conf             = conf.getChild("repo");
		this.bundlesFile = conf.getAttribute("bundles", "bundles.xml");
		this.local       = new File(conf.getAttribute("local"));
		logger.info("Local Repository: '{}'", local);
	}

	@Override
	public void initialize()
		throws Exception
	{
		localDescriptors  = getLocalDescriptors();
		remoteDescriptors = getRemoteDescriptors();
	}

	protected BundleDescriptors getLocalDescriptors()
		throws Exception
	{
		logger.info("Loading local descriptors");
		File file = new File(local, bundlesFile);
		if(!file.exists())
		{
			return new BundleDescriptorsImpl();
		}

		InputStream is = new FileInputStream(file);
		return parse(is);
	}

	protected BundleDescriptors parse(InputStream is)
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
			logger.info("\t* Adding bundle: id#{} version#{} file#{} hash#{}", id, version, file, hash);
			list.add(new BundleDescriptorImpl(id, version, file, hash));
		}

		logger.info("Loaded {} bundles", list.size());
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
				logger.info("Bundle id#{} requires update", id);
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
				File file = getAbsoluteFile(local);
				return !file.exists();
			}
			else
			{
				return true;
			}
		}
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

		logger.info("Updating bundle id#{}", id);
		
		File target = getAbsoluteFile(remote);
		if(target.exists())
		{
			logger.warn("Overriding local file '{}'", target);	
		}

		InputStream is   = toInputStream(remote);
		byte[]      data = IOUtils.toByteArray(is);

		String h1 = remote.getHash();
		String h2 = MD5Utils.md5Hash(data);
		if(!h1.equals(h2))
		{
			throw new NotImplementedYet("Hash mismatch for: " + id + " " + h1 + "/" + h2);
		}
		
		remote.setHash(h2);
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

	@Override
	public BundleDescriptor byId(String id)
	{
		return localDescriptors.byId(id);
	}

	@Override
	public BundleDescriptors getBundles()
	{
		return localDescriptors;
	}

	@Override
	public File getAbsoluteFile(BundleDescriptor descriptor)
	{
		String name = descriptor.getId() + File.separator + descriptor.getFile();
		return new File(local, name);
	}

	protected abstract BundleDescriptors getRemoteDescriptors()
		throws Exception;

	protected abstract InputStream toInputStream(BundleDescriptor desc)
		throws Exception;
}
