package br.com.ibnetwork.xingu.attributes;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 */
public interface AttributeType
{
	String getTypeName();
	
	Class getJavaType();
}
