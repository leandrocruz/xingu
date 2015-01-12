package xingu.lang;

import java.util.concurrent.Semaphore;

public class ThreadBlocker
{
    private final Semaphore sem  = new Semaphore(0);
    
    /**
     * Starts a new thread that keeps running until somebody releases the semaphore.
     * Handy if you want to hold the JVM after the main thread is gone 
     */
    public void createThreadAndHold()
    {
        Thread t = new Thread(new Runnable(){

            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        sem.acquire();
                        break;
                    }
                    catch (InterruptedException e)
                    {}
                }
            }
        }, "ThreadBlocker");
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }
    
    public void letGo()
    {
        sem.release();
    }
}
