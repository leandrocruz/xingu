package xingu.update.impl;

import org.apache.commons.lang3.StringUtils;

import xingu.update.BundleDescriptor;

public class BundleDescriptorImpl
	implements BundleDescriptor
{
	private String	id;

	private String	file;

	private String	hash;

	private String	currentVersion;
	
	private String	lastVersion;

	public BundleDescriptorImpl(String id, String version, String file, String hash)
	{
		this.id   = id;
		this.file = file;
		this.hash = hash;
		this.currentVersion = version;
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
	@Override public String getCurrentVersion(){return currentVersion;}
	public void setCurrentVersion(String version){this.currentVersion = version;}
	@Override public String getHash(){return hash;}
	public void setHash(String hash){this.hash = hash;}
	@Override public String getLastVersion(){return lastVersion;}
	@Override public void setLastVersion(String version){this.lastVersion = version;}

	@Override
	public boolean isLastVersion()
	{
		return StringUtils.equals(currentVersion, lastVersion);
	}
}
