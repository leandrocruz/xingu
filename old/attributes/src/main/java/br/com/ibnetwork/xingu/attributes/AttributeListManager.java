package br.com.ibnetwork.xingu.attributes;

import java.util.List;
import java.util.Map;

public interface AttributeListManager
{
	String DEFAULT_GROUP_NAME = "default";
	
	String DEFAULT_REPO_NAME = "default";
	
    AttributeList getListById(long id)
		throws AttributeException;

    AttributeList getListById(Class clazz, long id)
        throws AttributeException;

    AttributeList getListById(String nameSpace, long id)
		throws AttributeException;
    
    AttributeList create(Map map)
		throws AttributeException;

    AttributeList create(long id, List<Attribute> attributes)
        throws AttributeException;
    
	Attribute create(String name, Object value)
		throws AttributeException;
	
	long store(AttributeList list)		
		throws AttributeException;

    long store(Class clazz, AttributeList list)        
        throws AttributeException;

    long store(String nameSpace, AttributeList list)		
		throws AttributeException;
    
	void remove(long id)		
		throws AttributeException;

    void remove(Class clazz, long id)      
        throws AttributeException;

    void remove(String nameSpace, long id)		
		throws AttributeException;
}
