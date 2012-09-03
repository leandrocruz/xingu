package br.com.ibnetwork.xingu.lang;

import java.io.File;
import java.util.concurrent.Semaphore;

public class Sys 
{
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
