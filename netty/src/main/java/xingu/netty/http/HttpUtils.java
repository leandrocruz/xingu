package xingu.netty.http;

import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.HOST;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.base64.Base64;
import org.jboss.netty.handler.codec.compression.EmbeddedDeflater;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

import xingu.lang.NotImplementedYet;
import xingu.netty.Deflater;
import xingu.url.QueryString;
import xingu.url.Url;
import xingu.url.UrlParser;
import xingu.url.impl.QueryStringImpl;
import xingu.utils.CharUtils;
import xingu.utils.NettyUtils;
import xingu.utils.collection.FluidMap;

public class HttpUtils
{
    public static final String HTTP = "http://";
    
    public static final String HTTPS = "https://";

    public static Pattern pattern = Pattern.compile("< *meta http-equiv *= *[\"'] *Content\\-Type *[\"'] *content *= *[\"'] *([a-zA-Z0-9 \\-=;/]+) *[\"']", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    /* http://www.w3.org/Protocols/rfc2616/rfc2616.txt */
    public static String DEFAULT_HTTP_CHARSET_NAME = "ISO-8859-1";

    public static Charset DEFAULT_HTTP_CHARSET = Charset.forName(DEFAULT_HTTP_CHARSET_NAME);

    public static String urlFrom(HttpRequest request)
    {
        if(request == null)
        {
            return null;
        }

        String path = request.getUri();
        if(path.startsWith(HTTP))
        {
            return path;
        }
        String host = request.getHeader(HttpHeaders.Names.HOST);
        return HTTP+host+path;
    }

    public static boolean isUrlEncoded(String type)
    {
    	if(type == null)
    	{
    		return false;
    	}
    	int idx = type.indexOf("application/x-www-form-urlencoded");
		return idx >= 0;
    }
    
    public static String charset(String input, String deflt)
    {
    	return fieldFrom("charset", input, deflt);
    }
    
    public static String fieldFrom(String field, String input, String deflt)
    {
        if(input == null)
        {
            return deflt;
        }
        int idx = input.indexOf(field);
        if(idx >= 0)
        {
            String name = input.substring(idx + field.length());
            name = StringUtils.strip(name, " ='\";");
            if(StringUtils.isEmpty(name))
            {
                name = null;
            }
            return name;
        }
        return deflt;
    }

    public static String toString(Object obj, Encoding encoding, String charset)
        throws IOException
    {
        ChannelBuffer buffer = NettyUtils.bufferFrom(obj);
        InputStream is = toInputStream(buffer, encoding);
        String content = IOUtils.toString(is, charset);
        return content;
    }


    public static InputStream toInputStream(ChannelBuffer buffer, Encoding encoding)
        throws IOException
    {
        if(buffer == null)
        {
            return null;
        }
        byte[] array = buffer.array();
        InputStream is = new ByteArrayInputStream(array);
        switch (encoding)
        {
            case GZIP:
                return new GZIPInputStream(is);
            case ZIP:
                return new ZipInputStream(is);
            case DEFLATE:
                return new InflaterInputStream(is);
        }
        return is;
    }
    
    public static boolean isWebSocketUpgrade(Object msg)
	{
		 if(msg instanceof HttpResponse == false)
		 {
			 return false;
		 }

		 HttpResponse res = (HttpResponse) msg;
		 String connection = res.getHeader(HttpHeaders.Names.CONNECTION);
		 String upgrade = res.getHeader(HttpHeaders.Names.UPGRADE);
		 return HttpHeaders.Values.WEBSOCKET.equalsIgnoreCase(upgrade)
				 && HttpHeaders.Values.UPGRADE.equalsIgnoreCase(connection);
	}

    public static ChannelBuffer toChannelBuffer(String content, String contentType, Encoding encoding, String charset)
        throws IOException
    {
        PipedInputStream pipeIn = new PipedInputStream();
        OutputStream os = null;
        switch (encoding)
        {
            case GZIP:
                os = new GZIPOutputStream(new PipedOutputStream(pipeIn));
                break;
            case ZIP:
                os = new ZipOutputStream(new PipedOutputStream(pipeIn));
                break;
            case DEFLATE:
                os = new InflaterOutputStream(new PipedOutputStream(pipeIn));
                break;
        }
        byte[] data;
        if(os == null)
        {
            data = content.getBytes(charset);
        }
        else
        {
            byte[] array  = charset == null ? content.getBytes() : content.getBytes(charset);
            IOUtils.write(array, os);
            os.flush();
            os.close();
            data = IOUtils.toByteArray(pipeIn);
            pipeIn.close();
        }
        ChannelBuffer result = ChannelBuffers.wrappedBuffer(data);
        return result;
    }

    public static String charsetFrom(String text)
    {
        Matcher m = pattern.matcher(text);
        if(!m.find())
        {
            return null;
        }
        String value = m.group(1);
        return charset(value, null);
    }

    public static QueryString queryStringFrom(HttpRequest req)
	{
		String uri = req.getUri();
		int    idx = uri.indexOf("?");
		if(idx < 0)
		{
			return QueryStringImpl.EMPTY;
		}

		String in = uri.substring(idx + 1);
		return new QueryStringImpl(in);
	}

    public static Map<String, String[]> parseQueryString(String input /* escaped */)
    {
        Map<String, String[]> result = new HashMap<String, String[]>();
        String values[] = null;
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(input, "&");
        while (st.hasMoreTokens())
        {
            String pair = st.nextToken();
            int pos = pair.indexOf('=');
            if (pos < -1)
            {
                result.put(pair, null);
                continue;
            }
            String key = parseName(pair.substring(0, pos), sb);
            String val = parseName(pair.substring(pos+1, pair.length()), sb);
            if (result.containsKey(key))
            {
                String oldVals[] = result.get(key);
                values = new String[oldVals.length + 1];
                for (int i = 0; i < oldVals.length; i++)
                {
                    values[i] = oldVals[i];
                }
                values[oldVals.length] = val;
            }
            else
            {
                values = new String[1];
                values[0] = val;
            }
            result.put(key, values);
        }
        return result;
    }

    private static String parseName(String s, StringBuffer sb)
    {
        sb.setLength(0);
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            switch (c)
            {
                case '+':
                    sb.append(' ');
                    break;
                case '%':
                    int lenght;
                    char type = s.charAt(i + 1);
                    String input;

                    // Not supporting Unicode yet
                    if(type == 'u')
                    {
                        input = "20";
                        lenght = 5;
                    }
                    else
                    {
                        input = s.substring(i + 1, i + 3);
                        lenght = 2;
                    }

                    try
                    {
                        sb.append((char) Integer.parseInt(input, 16));
                        i += lenght;
                    }
                    catch (NumberFormatException e)
                    {
                        // XXX
                        // need to be more specific about illegal arg
                        throw new IllegalArgumentException("value: " + s + "\r\npart: " + input);
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        String rest = s.substring(i);
                        sb.append(rest);
                        if (rest.length() == lenght)
                        {
                            i++;
                        }
                    }

                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static void replaceMessageContent(Object msg, ChannelBuffer buffer)
    {
        if (msg instanceof HttpResponse)
        {
            HttpResponse response = (HttpResponse) msg;
            response.setContent(buffer);
            response.setHeader(CONTENT_LENGTH, buffer.capacity());
        }
        else if (msg instanceof HttpChunk)
        {
            HttpChunk chunk = (HttpChunk) msg;
            chunk.setContent(buffer);
        }
        else
        {
            throw new NotImplementedYet();
        }
    }

    public static Deflater deflater(Encoding encoding)
    {
        switch (encoding)
        {
            case GZIP:
                return new EmbeddedDeflater(ZlibWrapper.GZIP);
            case ZIP:
                return new EmbeddedDeflater(ZlibWrapper.ZLIB);
            case DEFLATE:
                return new EmbeddedDeflater(ZlibWrapper.NONE);
        }
        return null;
    }

    public static String removeValueFromHeader(HttpRequest request, String header, String value)
    {
        String h = request.getHeader(header);
        if(StringUtils.isEmpty(h) || h.indexOf(value) < 0)
        {
            return null;
        }

        String result = null;
        boolean foundHeader = false;
        String[] parts = StringUtils.split(h, ",");
        for (int i = 0; i < parts.length; i++)
        {
            String part = parts[i];
            if(value.equalsIgnoreCase(part))
            {
                foundHeader = true;
                parts[i] = null;
            }
        }

        if(foundHeader)
        {
            result = xingu.utils.StringUtils.joinIgnoringNulls(parts, ",");
        }
        return result;
    }

    public static ChannelBuffer encode(HttpResponse response, ChannelBufferFactory factory)
    	throws Exception
    {
        ChannelBuffer header = ChannelBuffers.dynamicBuffer(factory);
        encodeInitialLine(response, header);
        encodeHeaders(response, header);

        header.writeByte(CharUtils.CR);
        header.writeByte(CharUtils.LF);

        boolean chunked = isTransferEncodingChunked(response);
        ChannelBuffer content = response.getContent();
        if (!content.readable())
        {
        	// no content
            return header;
        }
        else if (chunked)
        {
            throw new IllegalArgumentException("HttpMessage content must be empty if Transfer-Encoding is chunked.");
        }
        else
        {
            return wrappedBuffer(header, content);
        }
    }

	public static boolean isTransferEncodingChunked(HttpMessage message)
	{
        List<String> headers = message.getHeaders(HttpHeaders.Names.TRANSFER_ENCODING);
        if (headers.isEmpty())
        {
            return false;
        }

        for (String header: headers)
        {
            if (HttpHeaders.Values.CHUNKED.equalsIgnoreCase(header))
            {
                return true;
            }
        }
        return false;
	}

	private static void encodeInitialLine(HttpResponse response, ChannelBuffer buffer)
		throws Exception
	{
        buffer.writeBytes(response.getProtocolVersion().toString().getBytes("ASCII"));
        buffer.writeByte(CharUtils.SP);
        buffer.writeBytes(String.valueOf(response.getStatus().getCode()).getBytes("ASCII"));
        buffer.writeByte(CharUtils.SP);
        buffer.writeBytes(String.valueOf(response.getStatus().getReasonPhrase()).getBytes("ASCII"));
        buffer.writeByte(CharUtils.CR);
        buffer.writeByte(CharUtils.LF);
	}

	private static void encodeHeaders(HttpMessage message, ChannelBuffer header)
		throws Exception
	{
        for (Map.Entry<String, String> h: message.getHeaders())
        {
        	encodeHeader(header, h.getKey(), h.getValue());
        }
	}

	private static void encodeHeader(ChannelBuffer buffer, String headerName, String value)
		throws Exception
	{
		buffer.writeBytes(headerName.getBytes("ASCII"));
		buffer.writeByte(CharUtils.COLON);
		buffer.writeByte(CharUtils.SP);
		buffer.writeBytes(value.getBytes("ASCII"));
		buffer.writeByte(CharUtils.CR);
		buffer.writeByte(CharUtils.LF);
	}

	public static final String hostFrom(HttpRequest req)
	{
		String host = req.getHeader(HOST);
		if(host != null)
		{
			return host;
		}
		
		String      uri     = req.getUri();
		HttpVersion version = req.getProtocolVersion();
		boolean     v1_0    = HttpVersion.HTTP_1_0.equals(version);
		if(v1_0)
		{
			boolean startsWithProto = uri.startsWith(HTTP) || uri.startsWith(HTTPS);
			if(startsWithProto)
			{
				Url url = UrlParser.parse(uri);
				return url.getHost();
			}
		}
		throw new NotImplementedYet("Can't retrieve host from version: '" +version + "', host: '" + host + "', uri: '" + uri + "'");
	}

	public static boolean isMultipartFormData(HttpRequest req)
	{
		String cType = req.getHeader(HttpHeaders.Names.CONTENT_TYPE);
		return cType == null ? false : cType.startsWith("multipart/form-data");
	}
	
	public static final String[] authFrom(HttpRequest httpRequest)
	{
		String scheme = "Basic";
		String auth   = httpRequest.getHeader(HttpHeaders.Names.AUTHORIZATION);
		if(StringUtils.isEmpty(auth) || auth.indexOf(scheme) < 0)
		{
			return null;
		}
		
		auth                  = auth.substring(scheme.length());
		auth                  = auth.trim();
		ChannelBuffer encoded = ChannelBuffers.wrappedBuffer(auth.getBytes());
		ChannelBuffer decoded = Base64.decode(encoded);
		String        up      = decoded.toString(Charset.forName("utf8"));
		if(StringUtils.isEmpty(up))
		{
			return null;
		}
		
		String[] pair = up.split(":");
		return pair;
	}

	public static FluidMap<String> parsePostData(HttpRequest req)
		throws Exception
	{
		FluidMap<String> map = new FluidMap<String>();
		ChannelBuffer buffer = req.getContent();
		if(buffer == null)
		{
			return map;
		}
		int readable = buffer.readableBytes();
		if(readable <= 0)
		{
			return map;
		}

		boolean multipart = HttpUtils.isMultipartFormData(req);
		if(multipart)
		{
			List<InterfaceHttpData> items = HttpUtils.whenMultipart(req);
			List<Attribute> attributes = HttpUtils.attributesFrom(items);
			for(Attribute attr : attributes)
			{
				map.add(attr.getName(), attr.getValue());
			}
			return map;
		}
		else
		{
			List<InterfaceHttpData> items = HttpUtils.whenPostData(req);
			List<Attribute> attributes = HttpUtils.attributesFrom(items);
			for(Attribute attr : attributes)
			{
				map.add(attr.getName(), attr.getValue());
			}
			return map;
		}

//		if(true)
//		{
//			throw new NotImplementedYet();
//		}
//		String  cType        = req.getHeader(HttpHeaders.Names.CONTENT_TYPE);
//		String  csName       = HttpUtils.charset(cType, HttpUtils.DEFAULT_HTTP_CHARSET_NAME);
//		Charset charset      = Charset.forName(csName);
//		String  data         = buffer.toString(charset);
//		boolean isUrlEncoded = HttpUtils.isUrlEncoded(cType);
//		if(isUrlEncoded)
//		{
//			data = URLDecoder.decode(data, csName);
//		}
//				
//		return map;
	}

	public static List<InterfaceHttpData> whenMultipart(HttpRequest req)
		throws Exception
	{
		HttpDataFactory         factory = new DefaultHttpDataFactory();
		HttpPostRequestDecoder  decoder = new HttpPostRequestDecoder(factory, req);
		List<InterfaceHttpData> items   = decoder.getBodyHttpDatas();
		return items;
	}
	
	public static List<InterfaceHttpData> whenPostData(HttpRequest req)
		throws Exception
	{
		HttpDataFactory         factory = new DefaultHttpDataFactory();
		HttpPostRequestDecoder  decoder = new HttpPostRequestDecoder(factory, req);
		List<InterfaceHttpData> items   = decoder.getBodyHttpDatas();
		return items;
	}

	public static List<Attribute> attributesFrom(List<InterfaceHttpData> items)
	{
		List<Attribute> result = new ArrayList<Attribute>();
		for(InterfaceHttpData item : items)
		{
			HttpDataType type = item.getHttpDataType();
			switch(type)
			{
				case Attribute:
					Attribute attr = (Attribute) item;
					result.add(attr);
					break;
				
				default:
					break;
			}
		}
		return result;
	}

	public static final void print(List<InterfaceHttpData> items)
		throws Exception
	{
		for (InterfaceHttpData item : items)
		{
			HttpDataType type = item.getHttpDataType();
			String       name = item.getName();
			System.out.println("Type: " + type + ", Name: " + name);
			switch(type)
			{
				case FileUpload:
					FileUpload upload      = (FileUpload) item;
					Charset    charset     = upload.getCharset();
					String     enc         = upload.getContentTransferEncoding();
					String     contentType = upload.getContentType();
					String     filename    = upload.getFilename();
					System.out.println(charset + " " + enc + " " + contentType + " " + filename);
					break;

				case Attribute:
					Attribute attr = (Attribute) item;
					System.out.println(attr.getName() + " = " + attr.getValue());
					break;

				default:
					break;
			}
		}
	}
}