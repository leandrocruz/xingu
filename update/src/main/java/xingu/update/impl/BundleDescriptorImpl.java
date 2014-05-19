package xingu.update.impl;

import xingu.update.BundleDescriptor;

public class BundleDescriptorImpl
	implements BundleDescriptor
{
	private String	id;

	private String	file;

	private String	hash;

	private String	version;

	public BundleDescriptorImpl(String id, String version, String file, String hash)
	{
		this.id      = id;
		this.version = version;
		this.file    = file;
		this.hash    = hash;
	}

	@Override
	public String toString()
	{
		return id + " @ " + file;
	}

	@Override public String getId(){return id;}
	public void setId(String id){this.id = id;}
	@Override public String getFile(){return file;}
	public void setFile(String file){this.file = file;}
	@Override public String getVersion(){return version;}
	public void setVersion(String version){this.version = version;}
	@Override public String getHash(){return hash;}
	@Override public void setHash(String hash){this.hash = hash;}
}
