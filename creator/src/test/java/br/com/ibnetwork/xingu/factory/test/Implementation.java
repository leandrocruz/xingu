package br.com.ibnetwork.xingu.factory.test;


/**
 * @author leandro
 */
public class Implementation
	extends UsesFactory    
	implements Interface
{

    private String s;
    
    private int i;
    
    public Implementation()
    {
        
    }
    public Implementation(String s, int i)
    {
        this.i = i;
        this.s = s;
    }
    public String getString()
    {
        return s;
    }
    public int getInteger()
    {
        return i;
    }
    
}
