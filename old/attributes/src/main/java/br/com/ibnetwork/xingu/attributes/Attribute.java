package br.com.ibnetwork.xingu.attributes;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 */
public interface Attribute
{
    long getId();
    
    long getListId();
    
    void setListId(long id);
    
    AttributeType getType();
	
	Object getValue();

    String getStoredValue();    
}
