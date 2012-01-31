package br.com.ibnetwork.xingu.network.impl.local;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import br.com.ibnetwork.xingu.network.Crashable;

public class Buffer
    implements Crashable
{
    private Queue<Object> clientToServer = new LinkedBlockingQueue<Object>();
    
    private Queue<Object> serverToClient = new LinkedBlockingQueue<Object>();
    
    private boolean crashed;
    

    public void toServer(Object message)
        throws IOException
    {
        put(message, clientToServer);
    }

    public void toClient(Object message)
        throws IOException
    {
        put(message, serverToClient);
    }

    public Object fromClient()
        throws IOException
    {
        return get(clientToServer);
    }

    public Object fromServer()
        throws IOException
    {
        return get(serverToClient);
    }

    public Object get(Queue<Object> queue) 
        throws IOException
    {
        Object obj = null;
        synchronized(queue)
        {
            while(!crashed && (obj = queue.poll()) == null)
            {
                try
                {
                    queue.wait();
                }
                catch (InterruptedException e)
                {}
            }
        }
        if(obj == null)
        {
            throw new IOException("Can't read object from network. Network crashed?");
        }
        return obj;
    }

    public void put(Object obj, Queue<Object> queue)
        throws IOException
    {
        if(crashed)
        {
            throw new IOException("Can't put object on network. Network crashed?");
        }
        synchronized (queue)
        {
            queue.offer(obj);
            queue.notifyAll();
        }
    }

    @Override
    public void crash()
    {
        crashed = true;
    }

    @Override
    public void resume()
    {
        crashed = false;
    }

    @Override
    public boolean isCrashed()
    {
        return crashed;
    }
}