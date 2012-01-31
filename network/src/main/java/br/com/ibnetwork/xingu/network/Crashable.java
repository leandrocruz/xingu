package br.com.ibnetwork.xingu.network;

public interface Crashable
{
    boolean isCrashed();
    
    void crash();
    
    void resume()
        throws Exception;
}
