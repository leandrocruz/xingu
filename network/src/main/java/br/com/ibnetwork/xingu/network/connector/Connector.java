package br.com.ibnetwork.xingu.network.connector;

import br.com.ibnetwork.xingu.network.PipeHandler;

public interface Connector
{
    void register(PipeHandler handler);
    
    String alias();
}
