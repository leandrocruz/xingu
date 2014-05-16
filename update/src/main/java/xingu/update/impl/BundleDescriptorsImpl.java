package xingu.update.impl;

import java.util.Iterator;
import java.util.List;

import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;

public class BundleDescriptorsImpl
	implements BundleDescriptors
{
	private final List<BundleDescriptor> descriptors;
	
	public BundleDescriptorsImpl(List<BundleDescriptor> descriptors)
	{
		this.descriptors = descriptors;
	}
	
	@Override
	public Iterator<BundleDescriptor> iterator()
	{
		return descriptors.iterator();
	}

	@Override
	public BundleDescriptor byId(String id)
	{
		for(BundleDescriptor desc : descriptors)
		{
			if(id.equals(desc.getId()))
			{
				return desc;
			}
		}
		return null;
	}

	@Override
	public int size()
	{
		return descriptors.size();
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("Total of '");
		sb
			.append(descriptors.size())
			.append("' descriptors: \n");
		
		for(BundleDescriptor desc : descriptors)
		{
			sb.append("\tid: ").append(desc.getId()).append(", file: ").append(desc.getFile()).append("\n");
		}

		return sb.toString();
	}

}
