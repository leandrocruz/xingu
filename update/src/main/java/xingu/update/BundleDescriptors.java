package xingu.update;

import java.util.Iterator;

public interface BundleDescriptors
{
	Iterator<BundleDescriptor> iterator();

	BundleDescriptor byId(String id);

	int size();
}