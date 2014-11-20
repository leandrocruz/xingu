package xingu.url;

import static org.junit.Assert.*;

import java.net.URLEncoder;

import org.junit.Test;

import com.google.common.net.UrlEscapers;

public class UrlEncoderTest
{
	@Test
    public void testEncode()
    	throws Exception
	{
		String  input    = "Setença";
		System.out.println(UrlEscapers.urlPathSegmentEscaper().escape(input));
		System.out.println(URLEncoder.encode(input));
		System.out.println(URLEncoder.encode(input, "ISO-8859-1"));
		System.out.println(URLEncoder.encode(input, "UTF-8"));
		
		
		assertEquals("Ac%F3rd%E3o", URLEncoder.encode("Acórdão", "ISO-8859-1"));
		assertEquals("C%EDvel", URLEncoder.encode("Cível", "ISO-8859-1"));
		
	}
}
