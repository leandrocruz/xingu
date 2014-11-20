package xingu.cloud.vm;

public interface SpawnRequest
{
	String getZone();
	
	String getProject();
	
	String getGroup();
	
	String getName();
	
	String getMachineType();
	
	String getImage();

	int getCount();
}