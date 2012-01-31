package br.com.ibnetwork.xingu.attributes;

import java.util.Collection;

import org.apache.avalon.framework.component.Component;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 */
public interface AttributeTypeManager
	extends Component
{
	public static final String ROLE = AttributeTypeManager.class.getName();
	 
	AttributeType getAttributeTypeByName(String type)
		throws AttributeTypeManagerException;

	AttributeType getIdType()	
		throws AttributeTypeManagerException;	
	
	Collection getAllTypes()
		throws AttributeTypeManagerException;
}
