package xingu.cloud.spawner;

import java.util.List;

import br.com.ibnetwork.xingu.utils.NameValue;

public interface SpawnRequest
{
	String getRegion();
	
	String getNamespace();
	
	String getGroup();
	
	String getNamePattern();

	String getIdPattern();
	
	String getMachineType();
	
	String getImage();

	int getCount();
	
	List<NameValue<String>> getMeta();
}