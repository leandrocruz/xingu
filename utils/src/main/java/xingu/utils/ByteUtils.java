package xingu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ByteUtils
{
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String toString(byte[] data)
	{
		return new String(data);
	}

	public static int toInt(byte[] data)
	{
		return ByteBuffer.wrap(data).getInt();
	}

	public static byte[] toByteArray(int number)
	{
		return ByteBuffer.allocate(4).putInt(number).array();
	}

	public static byte[] toByteArray(long number)
	{
		return ByteBuffer.allocate(8).putLong(number).array();
	}

	public static String toHex(byte[] data)
	{
		char[] hexChars = new char[data.length * 2];

		for(int j = 0; j < data.length; j++)
		{
			int        v         = data[j] & 0xFF;
			hexChars[j * 2]      = hexArray[v >>> 4];
			hexChars[j * 2  + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static final CharBuffer removeUnknowBytes(byte[] array, String charset)
		throws CharacterCodingException
	{
		CharsetDecoder utf8Decoder = Charset.forName(charset).newDecoder();
		utf8Decoder.onMalformedInput(CodingErrorAction.IGNORE);
		utf8Decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		ByteBuffer bytes = ByteBuffer.wrap(array);
		return utf8Decoder.decode(bytes);
	}

    public static byte[] compress(String name, byte[] data) 
        throws IOException
    {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);
        zipOut.putNextEntry(new ZipEntry(name));
        zipOut.write(data);
        zipOut.closeEntry();
        zipOut.close();
        
        byte[] result = baos.toByteArray();

        baos.close();
        return result;
    }
}
