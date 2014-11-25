package xingu.cloud.spawner;

public interface SpawnRequest
{
	String getZone();
	
	String getProject();
	
	String getGroup();
	
	String getNamePattern();

	String getIdPattern();
	
	String getMachineType();
	
	String getImage();

	int getCount();
}