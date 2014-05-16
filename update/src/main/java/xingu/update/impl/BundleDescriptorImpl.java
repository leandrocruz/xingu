package xingu.update.impl;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import xingu.update.BundleDescriptor;

public class BundleDescriptorImpl
	implements BundleDescriptor

{
	private String	id;

	private String	file;

	public BundleDescriptorImpl(String id, String file)
	{
		this.id   = id;
		this.file = file;
	}

	@Override
	public String toString()
	{
		return id + " @ " + file;
	}

	public String getId(){return id;}
	public void setId(String id){this.id = id;}
	public String getFile(){return file;}
	public void setFile(String file){this.file = file;}

	@Override
	public boolean isLastVersion()
	{
		throw new NotImplementedYet();
	}
}
