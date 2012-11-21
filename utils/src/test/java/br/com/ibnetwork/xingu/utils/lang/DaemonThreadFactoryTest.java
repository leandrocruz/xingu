package br.com.ibnetwork.xingu.utils.lang;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import br.com.ibnetwork.xingu.lang.thread.DaemonThreadFactory;

public class DaemonThreadFactoryTest
{
	private AtomicInteger total = new AtomicInteger(0);
	
	@Test
	public void testState()
		throws Exception
	{
		
		ThreadMXBean mgn = ManagementFactory.getThreadMXBean();
		int count = mgn.getDaemonThreadCount();
		//System.out.println("START: " + count);

		int threads = 50;
		int calls = 100;
		long interval = 50;
		
		final int sleepBeforeInterrupt = 2000; // should be smaller than 'interval * calls' ms
		final DaemonThreadFactory factory = new DaemonThreadFactory();

		
		
		CyclicBarrier barrier = new CyclicBarrier(threads + 1 /* main thread */);
		TestRunnable[] tasks = new TestRunnable[threads];
		Thread[] array = new Thread[threads];
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < threads; i++)
		{
			TestRunnable task = new TestRunnable("i"+i, calls, barrier, interval);
			tasks[i] = task;
			array[i] = factory.newThread(task);
			array[i].start();
		}

		final Object lock = new Object();
		
		new Thread(new Runnable(){

			@Override
			public void run()
			{
				try
				{
					Thread.sleep(sleepBeforeInterrupt);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				factory.interruptAllThreads();
				synchronized (lock)
				{
					lock.notify();
				}
			}
			
		}).start();
		
		synchronized (lock)
		{
			lock.wait();
		}

		//barrier.await(); //should last around 'interval * calls' ms
		
		//System.out.println("Done with: '" + total + "' calls after '" + (System.currentTimeMillis() - start) + "' ms");
		for (int i = 0; i < threads; i++)
		{
			Thread t = array[i];
			//System.out.println("Done " + t.getId() + " '" + t.getState() + "' " + t.isInterrupted() + "/" + t.isAlive());
		}
		
		//System.out.println("DONE: " + mgn.getDaemonThreadCount());
		//System.out.println("DONE: " + mgn.getDaemonThreadCount());
		
		//assertEquals(threads * calls, total.get());
		
//		for (int j = 0; j < threads; j++)
//		{
//			int herCalls = tasks[j].calls();
//			if(calls != herCalls)
//			{
//				System.out.println("** Thread "+j+" got "+herCalls+" calls");
//			}
//		}
	}
	
	class TestRunnable
		implements Runnable
	{
		
		private int size;
		
		private int calls = 0;

		private final CyclicBarrier barrier;

		private final long interval;
		
		public TestRunnable(String name, int size, CyclicBarrier barrier, long interval)
		{
			this.size = size;
			this.barrier = barrier;
			this.interval = interval;
		}
		
		public int calls()
		{
			return calls;
		}

		@Override
		public void run()
		{
			while(calls < size)
			{
				calls++;
				total.incrementAndGet();
				RandomStringUtils.random(1000000);
				try
				{
					Thread.sleep(interval);
				}
				catch (InterruptedException e)
				{
					return;
				}
			}
//			try
//			{
//				barrier.await();
//			}
//			catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
//			catch (BrokenBarrierException e)
//			{
//				e.printStackTrace();
//			}
		}
	}
}