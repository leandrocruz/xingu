package br.com.ibnetwork.xingu.network.impl;

import br.com.ibnetwork.xingu.network.Pipe;
import br.com.ibnetwork.xingu.network.PipeHandler;

public class NullPipeHandler
    implements PipeHandler
{
    
    @Override
    public void exceptionCaught(Pipe pipe, Throwable cause) 
        throws Exception
    {
        //System.out.println("exceptionCaught");
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(Pipe pipe, Object message) 
        throws Exception
    {
        //System.out.println("messageReceived");
    }

    @Override
    public void messageSent(Pipe pipe, Object message) 
        throws Exception
    {
        //System.out.println("messageSent");
    }

    @Override
    public void pipeClosed(Pipe pipe) 
        throws Exception
    {
        //System.out.println("pipeClosed");
    }

    @Override
    public void pipeCreated(Pipe pipe) 
        throws Exception
    {
        //System.out.println("pipeCreated");
    }

    @Override
    public void pipeOpened(Pipe pipe) 
        throws Exception
    {
        //System.out.println("pipeOpened");
    }
}