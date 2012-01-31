package br.com.ibnetwork.xingu.network.impl.local;

import br.com.ibnetwork.xingu.network.Attachable;
import br.com.ibnetwork.xingu.network.Network;

public abstract class NetworkSupport
    implements Network
{
    @Override
    public void attach(Attachable device) 
        throws Exception
    {
        device.attachTo(this);
    }

    @Override
    public void dettach(Attachable device) 
        throws Exception
    {
        device.dettachFrom(this);
    }
}
