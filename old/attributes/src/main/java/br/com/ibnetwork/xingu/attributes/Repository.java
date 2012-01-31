package br.com.ibnetwork.xingu.attributes;

import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;

public interface Repository 
{

	String getRepositoryName();
	
	String getGroupName();
	
	Configuration getListConfiguration(long id)
		throws AttributeException;

	List<Configuration> getMatchingConfigurations(Attribute attr)
		throws AttributeException;
		
	long store(AttributeList attrList)
		throws Exception;
	
	String getFullPath(long id);

}
