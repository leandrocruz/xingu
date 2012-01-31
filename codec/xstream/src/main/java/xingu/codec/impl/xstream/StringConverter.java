package xingu.codec.impl.xstream;

import org.apache.commons.codec.binary.Base64;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class StringConverter 
    extends AbstractSingleValueConverter 
{

    @Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) 
	{
		boolean retVal = clazz.equals(String.class); 
		return retVal;
	}

	@Override
	public Object fromString(String s) 
	{
	    byte[] input = s.getBytes();
		byte[] output = Base64.decodeBase64(input);
		String string = new String(output);
		return string;
	}
	
	@Override
	public String toString(Object obj) 
	{
	    byte[] input = obj.toString().getBytes();
	    byte[] output = Base64.encodeBase64(input);
	    String result = new String(output);
		return result;
	}
}
