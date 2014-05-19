package xingu.update.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xingu.update.BundleDescriptor;
import xingu.update.BundleDescriptors;

public class BundleDescriptorsImpl
	implements BundleDescriptors
{
	private final List<BundleDescriptor> descriptors;

	public BundleDescriptorsImpl()
	{
		descriptors = new ArrayList<BundleDescriptor>();
	}

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
	public BundleDescriptor put(BundleDescriptor replacement)
	{
		String wanted = replacement.getId();
		Iterator<BundleDescriptor> it = iterator();	
		BundleDescriptor result = null;
		while(it.hasNext())
		{
			BundleDescriptor desc = it.next();
			String           id   = desc.getId();
			if(wanted.equals(id))
			{
				it.remove();
				result = desc;
				break;
			}
		}

		descriptors.add(replacement);
		return result;
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
