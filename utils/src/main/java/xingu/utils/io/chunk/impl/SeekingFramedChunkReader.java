package xingu.utils.io.chunk.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SeekingFramedChunkReader
	extends FramedChunkReaderImpl
{
	private int[] numbers = new int[]{5, 19, 28, 24, 3};

	public SeekingFramedChunkReader(File file)
		throws FileNotFoundException
	{
		super(file);
	}

	@Override
	protected long seek(long start)
		throws IOException
	{
		long position = start;
		int size = calculateRequired();
		
		while(hasCapacity(size))
		{
			boolean found = findSequence(position);
			if(found)
			{
				return position;
			}
			position++;
		}
		return -1;
	}

	private int calculateRequired()
	{
		int result = 4 /* for number 5 */;
		for(int i = 1 ; i < numbers.length ; i++)
		{
			int n = numbers[i];
			result += 4 + 1 + n; /* an offset */
		}
		return result;
	}

	private boolean findSequence(long position)
		throws IOException
	{
		source.seek(position);
		for(int number : numbers)
		{
			int n = source.readInt();
			if(n != number)
			{
				return false;
			}
			if(n != 5)
			{
				long jumpTo = source.getFilePointer() + 1 /* type byte */ + n;
				source.seek(jumpTo);
			}
		}
		return true;
	}
}
