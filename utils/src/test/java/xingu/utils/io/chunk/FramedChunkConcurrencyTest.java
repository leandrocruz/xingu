package xingu.utils.io.chunk;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

import xingu.utils.io.chunk.impl.FrameImpl;
import xingu.utils.io.chunk.impl.FramedChunkImpl;
import xingu.utils.io.chunk.impl.FramedChunkReaderImpl;
import xingu.utils.io.chunk.impl.FramedChunkWriterImpl;

public class FramedChunkConcurrencyTest
{
	private static final int CHUNK_COUNT = 1000;
	
	private static final int CONSUMER_COUNT = 5;

	@Test
	public void testConcurrency()
		throws Exception
	{
		List<FramedChunk> chunks = Collections.synchronizedList(new ArrayList<FramedChunk>(1000));
		for(int i = 0 ; i < CHUNK_COUNT ; i++)
		{
			FramedChunk chunk = new FramedChunkImpl(2);
			chunk.add(new FrameImpl(i));
			chunk.add(new FrameImpl("i is " + i));
			chunks.add(chunk);
		}
		File file = File.createTempFile("chunk-test-", ".tmp");
		FramedChunkWriter writer = new FramedChunkWriterImpl(file);
		
		CyclicBarrier barrier = new CyclicBarrier(CONSUMER_COUNT + 1);
		for(int i = 0 ; i < CONSUMER_COUNT; i++)
		{
			new Consumer(i, barrier, writer, chunks).start();
		}
	
		System.out.println("Waiting " + CONSUMER_COUNT + " consumers");
		barrier.await();
		System.out.println("Done: " + file);
		writer.close();
		
		FramedChunkReader reader = new FramedChunkReaderImpl(file);
		List<FramedChunk> read = reader.read();
		assertEquals(CHUNK_COUNT, read.size());
		reader.close();
	}
}

class Consumer
	extends Thread
{
	private int	id;

	private CyclicBarrier	barrier;

	private FramedChunkWriter	writer;

	private List<FramedChunk>	chunks;

	public Consumer(int id, CyclicBarrier barrier, FramedChunkWriter writer, List<FramedChunk> chunks)
	{
		this.id      = id;
		this.barrier = barrier;
		this.writer  = writer;
		this.chunks  = chunks;
	}

	@Override
	public void run()
	{
		try
		{
			while(!chunks.isEmpty())
			{
				FramedChunk chunk = chunks.remove(0);
				//System.out.println(id + " consuming " + chunk);
				writer.write(chunk);
			}
			barrier.await();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
