package xingu.utils;

public class HexUtils
{
	private static final String CHARS = "0123456789ABCDEF";

	public static String toHex(int d)
	{
		int    r    = d % 16;
		String c    = toChar(r);
		int    diff = d - r;
		if (diff == 0)
		{
			return c;
		}
		else
		{
			int next = diff/16;
			return toHex(next) + c;
		}
	}

	private static String toChar(int c)
	{
		int pos = Math.abs(c);
		return String.valueOf(CHARS.charAt(pos));
	}

	public static String toHex(long number)
	{
		byte[] bytes = ByteUtils.toByteArray(number);
		return toHex(bytes);
	}

	public static String toHex(byte bytes[]) 
    {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        int i;
        
        for (i = 0; i < bytes.length; i++) 
        {
            if ((bytes[i] & 0xff) < 0x10) 
            {
                buf.append("0");
            }
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

}
