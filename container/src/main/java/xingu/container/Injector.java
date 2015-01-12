package xingu.container;

public interface Injector
{
    void injectDependencies(Object obj) 
        throws Exception;
}