package br.com.ibnetwork.xingu.container;

public interface Injector
{
    void injectDependencies(Object obj) 
        throws Exception;
}