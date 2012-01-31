package br.com.ibnetwork.xingu.factory.test;

public class MultipleConstructors
{
    public String s;
    
    public Integer i;
    
    public Long l;
    
    public MultipleConstructors()
    {}
    
    public MultipleConstructors(String s)
    {
        this.s = s;
    }

    public MultipleConstructors(String s, Integer i)
    {
        this.s = s;
        this.i = i;
    }

    public MultipleConstructors(Long l)
    {
        this.l = l;
    }
}
