package br.com.ibnetwork.xingu.network;

public interface Attachable
{
    void attachTo(Network network)
        throws Exception;

    void dettachFrom(Network network)
        throws Exception;
}
