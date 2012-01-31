package br.com.ibnetwork.xingu.network;

import org.apache.commons.lang.StringUtils;

import br.com.ibnetwork.xingu.network.impl.NullPipeHandler;


public class Reverser
    extends NullPipeHandler
{
    @Override
    public void messageReceived(Pipe pipe, Object message) 
        throws Exception
    {
        String result = StringUtils.reverse(message.toString());
        pipe.write(result);
    }
}