package xingu.update;

public interface UpdateManager
{
	BundleDescriptors getUpdates()
		throws Exception;

	BundleDescriptor byId(String id);

	BundleDescriptor update(String id)
		throws Exception;
}
