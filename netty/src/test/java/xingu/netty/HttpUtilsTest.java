package xingu.netty;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.DirectChannelBufferFactory;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.junit.Test;

import xingu.netty.http.HttpResponseBuilder;
import xingu.netty.http.HttpUtils;


public class HttpUtilsTest
{
    @Test
    public void testCharsetExtractor()
        throws Exception
    {
        test("wanted", "<meta http-equiv=\"content-type\" content=\"text/html; charset=wanted\"/>");
        test("wanted", "<meta http-equiv=\"Content-Type\" Content=\"text/html; charset=wanted\"/>");
        test("wanted", "<meta http-equiv=\"content-type \" content=\"text/html; charset=wanted\"/>");
        test("wanted", "<meta http-equiv=\" content-type \" content=\"text/html; charset=wanted\"/>");
        test("wanted", "<meta http-equiv=\" content-type\" content=\"text/html; charset=wanted\"/>");
        test("wanted", "<meta http-equiv=\"content-type\" content=\"text/html;charset=wanted\"/>");
        test("wanted", "<meta http-equiv=\"content-type\" content=\"charset=wanted\"/>");
        test("wanted", "<meta http-equiv=\"content-type\" content=\"charset=wanted\" />");
        test("wanted", "<meta http-equiv=\"content-type\" content=\"charset=wanted\" />\n");
        test("wanted", "<meta http-equiv=\"content-type\" content=\"charset=wanted\" />\nTRASH");
        test("wanted", "<meta http-equiv=\"content-type\" content=\"charset=wanted\"TRASH/>");
        test("wanted", "TRASH<meta http-equiv=\"content-type\" content=\"charset=wanted\"/>");
        test("wanted", "TRASH<meta http-equiv=\"content-type\" content=\"charset=wanted\"TRASH/>");
        test("wanted", "< meta http-equiv=\"content-type\" content=\"charset=wanted\"/>");
        test("wanted", "<meta http-equiv = \"content-type\" content = \"charset=wanted\"/>");
        test(null, "<meta http-equiv=\"other\" content=\"charset=wanted\"/>");

        test("wanted", "<meta http-equiv='content-type' content='charset=wanted'/>");
        test(null, "<meta http-equiv='other' content='charset=wanted'/>");
    }

    private void test(String expected, String input)
    {
        String charset = HttpUtils.charsetFrom(input);
        assertEquals(expected, charset);
    }
    
    @Test
    public void testUnicodeUrlDecode()
    {
        String input = "name=YouTube%20-%20Free%20Kick%204%20C.RONALDO%u25EEBECKHAM%20training%204%20/10%20HD";
        HttpUtils.parseQueryString(input);
    }

    @Test
    public void testCharsetFromName()
        throws Exception
    {
        assertEquals("UTF-8", HttpUtils.charset("text/html; charset=UTF-8", null));
        assertEquals("UTF-8", HttpUtils.charset("text/html; charset = UTF-8", null));
        assertEquals("UTF-8", HttpUtils.charset("text/html; charset='UTF-8'", null));
        assertEquals("UTF-8", HttpUtils.charset("text/html; charset = 'UTF-8'", null));         
        assertEquals("UTF-8", HttpUtils.charset("text/html; charset=\"UTF-8\"", null));
        assertEquals("UTF-8", HttpUtils.charset("text/html; charset = \"UTF-8\"", null));
    }
    
    @Test
    public void testBadCharset()
        throws Exception
    {
        assertEquals(null, HttpUtils.charset("text/html; charset=", null));
        assertEquals(null, HttpUtils.charset("text/html; charset= ", null));
        assertEquals(null, HttpUtils.charset("text/html; charset =", null));
        assertEquals(null, HttpUtils.charset("text/html; charset = ", null));
    }

    @Test
    public void testMessageEncoding()
    	throws Exception
    {
    	HttpResponse response = HttpResponseBuilder.builder()
    			.withStatus(HttpResponseStatus.OK)
    			.withHeader("MyHeader", "some value")
    			.withContent("Payload")
    			.build();
    	ChannelBufferFactory factory = DirectChannelBufferFactory.getInstance();
    	ChannelBuffer buffer = HttpUtils.encode(response, factory);
    	String expected = "HTTP/1.1 200 OK\r\nMyHeader: some value\r\nContent-Length: 7\r\n\r\nPayload"; 
    	String actual = buffer.toString(Charset.forName("ISO-8859-1"));
    	assertEquals(expected, actual);
    }

    @Test
    public void testMessageEncodingHeadersOnly()
    	throws Exception
    {
    	HttpResponse response = HttpResponseBuilder.builder()
    			.withStatus(HttpResponseStatus.OK)
    			.withHeader("MyHeader", "some value")
    			.build();
    	ChannelBufferFactory factory = DirectChannelBufferFactory.getInstance();
    	ChannelBuffer buffer = HttpUtils.encode(response, factory);
    	String expected = "HTTP/1.1 200 OK\r\nMyHeader: some value\r\n\r\n"; 
    	String actual = buffer.toString(Charset.forName("ISO-8859-1"));
    	assertEquals(expected, actual);
    }
}