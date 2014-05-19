package xingu.update;

import java.io.File;

public interface UpdateManager
{
	BundleDescriptors getUpdates()
		throws Exception;

	BundleDescriptor byId(String id);

	BundleDescriptor update(String id)
		throws Exception;

	BundleDescriptors getBundles();

	File getAbsoluteFile(BundleDescriptor descriptor);
}
