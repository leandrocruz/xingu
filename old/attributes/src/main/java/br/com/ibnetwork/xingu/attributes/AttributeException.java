package br.com.ibnetwork.xingu.attributes;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 */
public class AttributeException 
	extends NestableRuntimeException
{
    public AttributeException()
    {
        super();
    }
    
    public AttributeException(String message)
    {
        super(message);
    }
    
    public AttributeException(Throwable t)
    {
        super(t);
    }

    public AttributeException(String message, Throwable t)
    {
        super(message, t);
    }
}
