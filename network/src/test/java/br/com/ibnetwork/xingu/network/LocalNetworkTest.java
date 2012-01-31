package br.com.ibnetwork.xingu.network;


public class LocalNetworkTest
    extends NetworkTestSupport
{
    @Override
    protected String getContainerFile()
    {
        return "pulga-network-local.xml";
    }
}