package xingu.update;

import java.io.File;

import xingu.http.client.HttpProgressListener;

public interface UpdateManager
{
	BundleDescriptors getUpdates()
		throws Exception;

	BundleDescriptor byId(String id);

	BundleDescriptor update(String id)
		throws Exception;

	BundleDescriptor update(String id, HttpProgressListener listener)
		throws Exception;

	BundleDescriptors getBundles();

	File getAbsoluteFile(BundleDescriptor descriptor);
}
