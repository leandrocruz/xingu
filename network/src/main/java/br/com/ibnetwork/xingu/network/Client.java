package br.com.ibnetwork.xingu.network;

import java.io.IOException;

import br.com.ibnetwork.xingu.network.connector.ClientConnector;

public interface Client
    extends Attachable
{
    /**
     * Sends a message to the server
     */
    AsyncEvent send(Object message) 
        throws Exception;

    Object sendAndWait(Object obj) 
        throws Exception;

    /**
     * Receives a message from the server. Let the client decide what to return if no message is available 
     */
    Object receive()
        throws IOException;

    ClientConnector connector();
}
