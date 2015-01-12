package xingu.factory.test;

public class InvisibleConstructors
{
    public String s;
    
    public Integer i;
    
    private InvisibleConstructors()
    {}

    InvisibleConstructors(String s) 
    {
        this();
        this.s = s;
    }

    protected InvisibleConstructors(Integer i)
    {
        this();
        this.i = i;
    }
}
