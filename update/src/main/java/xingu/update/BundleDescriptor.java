package xingu.update;

public interface BundleDescriptor
{
	String getId();
	
	String getVersion();

	String getFile();
	
	String getHash(); //md5, sha, etc

//	String getLastVersion();
//	void setLastVersion(String version);
//	boolean isLastVersion();
}