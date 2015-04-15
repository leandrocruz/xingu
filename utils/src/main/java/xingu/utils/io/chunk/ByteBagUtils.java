package xingu.utils.io.chunk;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import xingu.utils.io.chunk.impl.ByteArrayByteBag;

public class ByteBagUtils
{
	public static final String SPLIT = new String("\u01C0\u01C1"); // 0xC780 0xC781
	
	public static ByteBag toByteBag(String... array)
	{
		String s = StringUtils.join(array, SPLIT);
		return new ByteArrayByteBag(s);
	}

	public static String[] split(ByteBag bag)
		throws IOException
	{
		if(bag == null)
		{
			return null;
		}
		
		byte[] bytes = bag.getBytes();
		if(bytes == null || bytes.length == 0)
		{
			return null;
		}
		
		return split(new String(bytes));
	}

	public static String[] split(String input)
	{
		if(input == null)
		{
			return null;
		}
		
		if(input.length() == 0)
		{
			return new String[]{input};
		}
		
		return input.split(SPLIT);
	}
}
