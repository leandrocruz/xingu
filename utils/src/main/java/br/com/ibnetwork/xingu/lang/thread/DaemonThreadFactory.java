package br.com.ibnetwork.xingu.lang.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DaemonThreadFactory
    implements ThreadFactory
{
    private volatile AtomicInteger count = new AtomicInteger(0);
    
    private volatile boolean interrupted = false;
    
    private final ThreadNamer namer;

    private final int priority;
    
    private List<Thread> threads = new ArrayList<Thread>();
    
    public DaemonThreadFactory()
    {
    	this(new ThreadNamer() {
    		
    		@Override
            public String nameFor(int threadNumber)
            {
                return "DaemonThread #"+threadNumber + " from " + DaemonThreadFactory.class.getSimpleName();
            }
        });
    }

    public DaemonThreadFactory(ThreadNamer threadNamer)
    {
        this.namer = threadNamer;
        this.priority = Thread.NORM_PRIORITY;
    }

    public DaemonThreadFactory(ThreadNamer threadNamer, int priority)
    {
        this.namer = threadNamer;
        this.priority = priority;
    }

    @Override
    public synchronized Thread newThread(Runnable runnable)
    {
		if(interrupted)
		{
			throw new IllegalStateException("DaemonThreadFactory interrupted already");
		}
    	
        int num = count.incrementAndGet();
        Thread t = new Thread(runnable);
        threads.add(t);
        
        if(namer != null)
        {
        	String name = namer.nameFor(num); 
        	t.setName(name);
        }
        
        t.setDaemon(true);
        t.setPriority(priority);
        return t;
    }
    
    public synchronized void interruptAllThreads()
    {
    	interrupted = true;
        for(Thread t : threads)
        {
            try
            {
            	//System.out.println("Interrupting " + t.getId() + " '" + t.getState() + "'");
                t.interrupt();
            }
            catch(Throwable error)
            {
            	error.printStackTrace();
            }
        }
    }
}