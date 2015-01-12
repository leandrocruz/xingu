package xingu.netty.http;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import xingu.utils.StringUtils;

public class HttpRequestBuilder
{
    private final HttpRequest request;

    private HttpRequestBuilder(HttpRequest request)
    {
		this.request = request;
    }

    public static HttpRequestBuilder builder(HttpVersion version, HttpMethod method, String uri)
    {
    	HttpRequest request = new DefaultHttpRequest(version, method, uri);
        return new HttpRequestBuilder(request);
    }

    public HttpRequest build()
    {
        return request;
    }

    public HttpRequestBuilder withHeader(String name, Object value)
    {
        String old = request.getHeader(name);
        if(StringUtils.isNotEmpty(old))
        {
        	request.removeHeader(name);
        }
        request.addHeader(name, value);
        return this;
    }

    public HttpRequestBuilder withContentType(String name)
    {
        return withHeader(CONTENT_TYPE, name);
    }

    public HttpRequestBuilder withContent(byte[] content)
    {
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(content);
        return withContent(buffer);
    }

    public HttpRequestBuilder withContent(String content)
    {
        byte[] bytes = content.getBytes();
        return withContent(bytes);
    }

    public HttpRequestBuilder withContent(ChannelBuffer buffer)
    {
    	request.setContent(buffer);
        int length = buffer.capacity();
        withContentLength(length);
        return this;
    }

    public HttpRequestBuilder withContentLength(int length)
    {
        return withHeader(CONTENT_LENGTH, length);
    }

    public HttpRequestBuilder withChunks(boolean chunked)
    {
    	request.setChunked(chunked);
        return this;
    }
}