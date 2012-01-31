package br.com.ibnetwork.xingu.network;

public interface PipeHandler
{
    void pipeCreated(Pipe pipe) 
        throws Exception;

    void pipeOpened(Pipe pipe) 
        throws Exception;
    
    void pipeClosed(Pipe pipe) 
        throws Exception;

    void exceptionCaught(Pipe pipe, Throwable cause) 
        throws Exception;

    void messageReceived(Pipe pipe, Object message) 
        throws Exception;

    void messageSent(Pipe pipe, Object message) 
        throws Exception;
}