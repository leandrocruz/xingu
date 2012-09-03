package xingu.exception.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.exception.ExceptionHandler;
import br.com.ibnetwork.xingu.lang.Sys;

public abstract class AsyncExceptionHandler
    implements ExceptionHandler, Runnable, Configurable, Startable
{
    private Thread worker;
    
    private Queue<ExceptionItem> queue = new ConcurrentLinkedQueue<ExceptionItem>();
    
    protected boolean shouldRun = true;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    protected List<String> ignored;
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        Configuration[] ignores = conf.getChild("ignore").getChildren("exception");
        ignored = new ArrayList<String>(ignores.length);
        for (Configuration ignore : ignores)
        {
            ignored.add(ignore.getAttribute("name"));
        }
    }
    
    @Override
    public void start()
        throws Exception
    {
        worker = createWorker();
        worker.start();
    }

    protected Thread createWorker()
    {
        Thread result = new Thread(this);        
        result.setName("AsyncExceptionHandler Worker");
        result.setDaemon(true);
        result.setPriority(Thread.MIN_PRIORITY + 1);
        return result;
    }

    @Override
    public void stop()
        throws Exception
    {
        shouldRun = false;
        synchronized (queue)
        {
            queue.clear();
            queue.notify();
        }
        try
        {
            worker.interrupt();
        }
        catch(Throwable t)
        {}
        worker = null;
    }

    @Override
    public void handle(Throwable throwable)
    {
        handle(throwable, null);
    }

    @Override
    public void handle(Throwable throwable, Thread thread)
    {
        if(ignore(throwable))
        {
            return;
        }
        if(shouldRun)
        {
            synchronized (queue)
            {                           
                queue.offer(new ExceptionItem(throwable, thread));
                queue.notify();
            }
        }
    }

    protected boolean ignore(Throwable throwable)
    {
        String name = throwable.getClass().getName();
        for(String ignore : ignored)
        {
            if(name.equals(ignore))
            {
                return true;
            }
        }
        return false;
    }

    protected void onProcessError(ExceptionItem item, Throwable t)
    {
        logger.info("Error processing element '"+item+"'from queue", t);
    }

    @Override
    public void run()
    {
        while(shouldRun)
        {
            ExceptionItem item = null;
            synchronized (queue)
            {
                item = queue.poll();
                if(item == null)
                {
                	try
        			{
                		queue.wait();
        			}
        			catch (InterruptedException e)
        			{
        				return;
        			}
                    continue;
                }
            }
            
            if(shouldRun)
            {
                try
                {
                    process(item);
                }
                catch (Throwable t)
                {
                    onProcessError(item, t);
                }
            }
        }
    }

    protected abstract void process(ExceptionItem item)
        throws Exception;
}

class ExceptionItem
{
    final Throwable throwable;
    
    final Thread thread;

    public ExceptionItem(Throwable throwable, Thread thread)
    {
        this.throwable = throwable;
        this.thread = thread;
    }
}