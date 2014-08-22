package xavante.dispatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import xavante.Xavante;
import xavante.XavanteRequest;
import xavante.dispatcher.impl.RequestHandlerSupport;
import xingu.netty.http.HttpResponseBuilder;

public class DefaultRequestHandler
	extends RequestHandlerSupport
{
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");

	private static Map<String, String> mimeByExtension = new HashMap<String, String>();
	
	static {
		mimeByExtension.put("html",		"text/html");
		mimeByExtension.put("htm", 		"text/html");
		mimeByExtension.put("txt", 		"text/plain");
		mimeByExtension.put("css", 		"text/css");
		mimeByExtension.put("jpg",		"image/jpeg");
		mimeByExtension.put("gif", 		"image/gif");
		mimeByExtension.put("png",		"image/png");
		mimeByExtension.put("js", 		"application/javascript");
		mimeByExtension.put("json",		"application/json");
		mimeByExtension.put("xml",		"application/xml");
		mimeByExtension.put("pdf",		"application/pdf");
		mimeByExtension.put("zip",		"application/zip");
	}
	
	@Override
	public void handle(XavanteRequest xeq)
		throws Exception
	{
		HttpRequest request = xeq.getRequest();
		String      uri     = request.getUri();
		boolean     isRoot  = Xavante.isRoot(uri);
		if(isRoot)
		{
			uri = "/index.html";
		}
		
		HttpResponseStatus status        = HttpResponseStatus.OK;
		ChannelBuffer      content       = null;
		String             contentType   = "text/html";
		String             charset       = "UTF-8";
		int                contentLength = 0;
		
		File          root    = new File(".").getAbsoluteFile();
		File          file    = new File(root, uri);
		boolean      exists  = file.exists();
		if(exists)
		{
			boolean isFile = file.isFile();
			if(!isFile)
			{
				//TODO: directory listing, etc
				file = new File(file, "index.html");
			}
			InputStream is   = new FileInputStream(file);
			byte[]      data = IOUtils.toByteArray(is);
			IOUtils.closeQuietly(is);
			content          = ChannelBuffers.wrappedBuffer(data);
			contentType      = mimeFrom(file);
		}
		else
		{
			status = HttpResponseStatus.NOT_FOUND;
			byte[] data = ("File '" + uri + "' not found on this server").getBytes();
			content = ChannelBuffers.wrappedBuffer(data);
		}

		contentType = charset == null ? contentType : contentType + "; charset=" + charset;
		contentLength = content == null ? 0 : content.readableBytes();
		
		HttpResponse res = HttpResponseBuilder
				.builder(status)
				.withHeader(HttpHeaders.Names.SERVER, "Xavante 1.0")
				.withHeader(HttpHeaders.Names.DATE, df.format(new Date()))
				.withHeader(HttpHeaders.Names.CONTENT_TYPE, contentType)
				.withHeader(HttpHeaders.Names.CONTENT_LENGTH, contentLength)
				.withContent(content)
				.build();

		String conn = request.getHeader(HttpHeaders.Names.CONNECTION);
		ChannelFuture future = xeq.write(res);
		if(HttpHeaders.Values.CLOSE.equalsIgnoreCase(conn))
		{
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private String mimeFrom(File file)
	{
		String name   = file.getName();
		String ext    = FilenameUtils.getExtension(name);
		String result = mimeByExtension.get(ext);
		return result != null ? result : "text/plain";
	}
}