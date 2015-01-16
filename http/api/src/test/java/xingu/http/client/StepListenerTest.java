package xingu.http.client;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import xingu.http.client.impl.StepListener;

public class StepListenerTest
{
	@Test
	public void testListener()
		throws Exception
	{
		final AtomicInteger counter = new AtomicInteger(0);

		HttpProgressListener listener = new StepListener(10)
		{
			@Override
			protected void log(long total, long progress)
			{
				System.out.println(((double) progress/(double) total) * 100);
				counter.incrementAndGet();
			}
		};

		int total = 10000;
		for(int i=1 ; i<=total ; i++)
		{
			listener.on(total, i);
		}
		
		assertEquals(10, counter.get());
	}
}
