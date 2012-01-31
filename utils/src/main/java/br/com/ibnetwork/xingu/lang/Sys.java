package br.com.ibnetwork.xingu.lang;

import java.io.File;
import java.util.concurrent.Semaphore;

public class Sys 
{
	public static void acquire(Semaphore sem)
	{
        try 
    	{
			sem.acquire();
		}
    	catch (InterruptedException e) 
    	{}
	}
	
    @SuppressWarnings("static-access")
	public static void sleepWithoutInterruptions(long millis) 
    {
		try 
		{
			Thread.currentThread().sleep(millis);
		}
		catch (InterruptedException e)
		{}
	}

	public static void waitWithoutInterruptions(Object lock, long millis)
	{
		try 
		{
			lock.wait(millis);
		}
		catch (InterruptedException e)
		{}
	}

	public static void waitWithoutInterruptions(Object lock)
	{
		try
		{
			lock.wait();
		}
		catch (InterruptedException e) 
		{}
	}

	public static Thread startDaemon(Runnable runnable) 
	{
	    return startDaemon(runnable, null);
	}

	public static Thread startDaemon(Runnable runnable, String name)
	{
		Thread t = new Thread(runnable);
		if(name != null)
		{
		    t.setName(name);
		}
		t.setDaemon(true);
		t.start();
		return t;
	}

	public static int myPid()
	    throws Exception 
	{
		if (!isWindows()) 
		{
			String pid = new File("/proc/self").getCanonicalFile().getName();
			return Integer.parseInt(pid);
		}
		return 0;
	}

	public static boolean isWindows() 
	{
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}
}
