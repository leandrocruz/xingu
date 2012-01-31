package br.com.ibnetwork.xingu.attributes;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 */
public class AttributeTypeManagerException 
	extends NestableRuntimeException
{
    public AttributeTypeManagerException(String message)
    {
    	super(message);
    }

}
