package xingu.update;

public interface BundleDescriptor
{
	String getId();
	
	String getVersion();

	String getFile();
	
	String getHash();
	void setHash(String hash);
}