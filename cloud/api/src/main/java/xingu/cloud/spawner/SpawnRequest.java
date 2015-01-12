package xingu.cloud.spawner;

import java.util.List;

import xingu.utils.NameValue;

public interface SpawnRequest
{
	String getRegion();
	
	String getNamespace();
	
	String getGroup();
	
	String getIdPattern();
	
	String getMachineType();
	
	String getImage();

	int getCount();
	
	List<NameValue<String>> getMeta();
}