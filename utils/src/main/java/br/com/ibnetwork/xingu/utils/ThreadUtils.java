package br.com.ibnetwork.xingu.utils;

public class ThreadUtils
{
    public static void interrupt(Thread thread)
    {
        try
        {
            thread.interrupt();
        }
        catch(Throwable t)
        {}
    }
}
