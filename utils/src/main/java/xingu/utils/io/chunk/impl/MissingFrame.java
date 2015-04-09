package xingu.utils.io.chunk.impl;

import xingu.lang.NotImplementedYet;
import xingu.utils.io.chunk.Frame;

public class MissingFrame
	implements Frame
{
	private int	size;

	public MissingFrame(int size)
	{
		this.size = size;
	}

	@Override
	public byte getType()
	{
		return Frame.MISSING;
	}

	@Override
	public byte[] getPayload()
	{
		throw new NotImplementedYet("Missing '"+size+"' bytes from source");
	}
}