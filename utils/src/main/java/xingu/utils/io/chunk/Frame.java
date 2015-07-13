package xingu.utils.io.chunk;

import java.io.IOException;

public interface Frame
{
	byte	MISSING		= 0;

	byte	IN_MEMORY	= 1;

	byte	LAZY		= 2;
	
	byte getType();

	int getSize();

	byte[] getPayload()
		throws IOException;

	String readString()
		throws IOException;
}