package xingu.utils;

import org.jboss.netty.buffer.ChannelBuffer;

public class HexDump
{
	private static final String NEWLINE = String.format("%n");

	private static final String[] BYTE2HEX = new String[256];

	private static final String[] HEXPADDING = new String[16];

	private static final String[] BYTEPADDING = new String[16];

	private static final char[] BYTE2CHAR = new char[256];
	
	private static final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	static
	{
		int i;

		// Generate the lookup table for byte-to-hex-dump conversion
		for (i = 0; i < 10; i++)
		{
			StringBuilder buf = new StringBuilder(3);
			buf.append(" 0");
			buf.append(i);
			BYTE2HEX[i] = buf.toString();
		}
		for (; i < 16; i++)
		{
			StringBuilder buf = new StringBuilder(3);
			buf.append(" 0");
			buf.append((char) ('a' + i - 10));
			BYTE2HEX[i] = buf.toString();
		}
		for (; i < BYTE2HEX.length; i++)
		{
			StringBuilder buf = new StringBuilder(3);
			buf.append(' ');
			buf.append(Integer.toHexString(i));
			BYTE2HEX[i] = buf.toString();
		}

		// Generate the lookup table for hex dump paddings
		for (i = 0; i < HEXPADDING.length; i++)
		{
			int padding = HEXPADDING.length - i;
			StringBuilder buf = new StringBuilder(padding * 3);
			for (int j = 0; j < padding; j++)
			{
				buf.append("   ");
			}
			HEXPADDING[i] = buf.toString();
		}

		// Generate the lookup table for byte dump paddings
		for (i = 0; i < BYTEPADDING.length; i++)
		{
			int padding = BYTEPADDING.length - i;
			StringBuilder buf = new StringBuilder(padding);
			for (int j = 0; j < padding; j++)
			{
				buf.append(' ');
			}
			BYTEPADDING[i] = buf.toString();
		}

		// Generate the lookup table for byte-to-char conversion
		for (i = 0; i < BYTE2CHAR.length; i++)
		{
			if (i <= 0x1f || i >= 0x7f)
			{
				BYTE2CHAR[i] = '.';
			}
			else
			{
				BYTE2CHAR[i] = (char) i;
			}
		}
	}

	public static String toHex(byte[] bytes)
	{
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++)
		{
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static String toHex(ChannelBuffer buffer)
	{
		int length = buffer.readableBytes();
		int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
		StringBuilder sb = new StringBuilder(rows * 80);

        sb.append(
                NEWLINE + "         +-------------------------------------------------+"   +
                NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |"   +
                NEWLINE + "+--------+-------------------------------------------------+----------------+");

		final int start = buffer.readerIndex();
		final int end 	= buffer.writerIndex();

		int i;
		for (i = start; i < end; i++)
		{
			int relIdx = i - start;
			int relIdxMod16 = relIdx & 15;
			if (relIdxMod16 == 0)
			{
				sb.append(NEWLINE);
				sb.append(Long.toHexString(relIdx & 0xFFFFFFFFL | 0x100000000L));
				sb.setCharAt(sb.length() - 9, '|');
				sb.append('|');
			}
			sb.append(BYTE2HEX[buffer.getUnsignedByte(i)]);
			if (relIdxMod16 == 15)
			{
				sb.append(" |");
				for (int j = i - 15; j <= i; j++)
				{
					sb.append(BYTE2CHAR[buffer.getUnsignedByte(j)]);
				}
				sb.append('|');
			}
		}

		if ((i - start & 15) != 0)
		{
			int remainder = length & 15;
			sb.append(HEXPADDING[remainder]);
			sb.append(" |");
			for (int j = i - remainder; j < i; j++)
			{
				sb.append(BYTE2CHAR[buffer.getUnsignedByte(j)]);
			}
			sb.append(BYTEPADDING[remainder]);
			sb.append('|');
		}

		sb.append(NEWLINE + "+--------+-------------------------------------------------+----------------+");

		return sb.toString();
	}

}
