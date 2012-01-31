package br.com.ibnetwork.xingu.attributes;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 */
public interface AttributeList
{
	long getId();
	
	void setId(long id);

	int size();

    String getNameSpace();
    
    void setNameSpace(String nameSpace);

	Map toMap();

	boolean contains(AttributeType type);
	
	boolean contains(Attribute attribute);

	boolean isSuperSet(AttributeList otherAttrList);
	
	List<Attribute> getAttributes();
	
	Attribute get(AttributeType type);
	
	Attribute get(String name)
		throws AttributeException;

	void put(Attribute attr)
		throws AttributeException;

	void put(String name, Object value)
		throws AttributeException;

	void delete(Attribute attr)
		throws AttributeException;

    //hack
    void setListManager(AttributeListManager listManager);
}
