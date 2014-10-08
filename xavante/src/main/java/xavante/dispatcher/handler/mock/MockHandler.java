package xavante.dispatcher.handler.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import xavante.XavanteRequest;
import xavante.dispatcher.impl.RequestHandlerSupport;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.http.client.impl.curl.CurlResponseParser;
import xingu.netty.http.HttpResponseBuilder;
import xingu.netty.http.HttpUtils;
import xingu.url.Url;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.collection.FluidMap;

public class MockHandler
	extends RequestHandlerSupport
	implements Configurable
{
	private Map<String, List<ReponseBuilder>> buildersByPath = new HashMap<String, List<ReponseBuilder>>();
	
	private File root;
	
	public MockHandler(String path)
	{
		super(path);
	}

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		String root = conf.getAttribute("root");
		this.root = new File(root);
		
		Configuration[] builders = conf.getChildren("response");
		for(Configuration configuration : builders)
		{
			try
			{
				ReponseBuilder builder = toBuilder(configuration);
				String         path    = builder.getPath();
				List<ReponseBuilder> list = buildersByPath.get(path);
				if(list == null)
				{
					list = new ArrayList<ReponseBuilder>();
					buildersByPath.put(path, list);
				}
				list.add(builder);
			}
			catch(Exception e)
			{
				throw new ConfigurationException("", e);
			}
		}
	}

	private ReponseBuilder toBuilder(Configuration conf)
		throws Exception
	{
		String path  = conf.getAttribute("path");
		String to    = conf.getAttribute("to");
		Configuration[] children = conf.getChildren("param");
		FluidMap<String> map = new FluidMap<String>();
		for(Configuration child : children)
		{
			String name  = child.getAttribute("name");
			String value = child.getAttribute("value");
			map.add(name, value);
		}
		
		File         file = new File(root, to);
		InputStream  is   = new FileInputStream(file);
		HttpResponse res  = CurlResponseParser.responseFrom("", is);
		return new ReponseBuilderImpl(path, map, res);
	}

	@Override
	public void handle(XavanteRequest xeq)
		throws Exception
	{
		org.jboss.netty.handler.codec.http.HttpResponse result = null;
		HttpMethod method = xeq.getRequest().getMethod();
		if(HttpMethod.OPTIONS.equals(method))
		{
			result = handleOptions(xeq);
		}
		else if(HttpMethod.GET.equals(method) || HttpMethod.POST.equals(method))
		{
			result = handleGetOrPost(xeq);
		}
		else
		{
			throw new NotImplementedYet();
		}
		xeq.write(result).addListener(ChannelFutureListener.CLOSE);
	}

	private org.jboss.netty.handler.codec.http.HttpResponse toNettyResponse(HttpResponse res)
		throws Exception
	{
		int code = res.getCode();
		HttpResponseStatus status = HttpResponseStatus.valueOf(code);
		DefaultHttpResponse result = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);

		NameValue[] headers = res.getHeaders();
		for(NameValue header : headers)
		{
			result.addHeader(header.getName(), header.getValue());
		}

		InputStream raw = res.getRawBody();
		raw.reset();
		byte[] array = IOUtils.toByteArray(raw);
		ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(array);
		result.setContent(buffer);
		return result;
	}

	private org.jboss.netty.handler.codec.http.HttpResponse handleOptions(XavanteRequest xeq)
		throws Exception
	{
		/*
		 * CORS request. See:
		 * - http://stackoverflow.com/questions/21850454/how-to-make-xmlhttprequest-cross-domain-withcredentials-http-authorization-cor
		 * - http://mortoray.com/2014/04/09/allowing-unlimited-access-with-cors/
		 * - http://www.html5rocks.com/en/tutorials/cors/
		 * - https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS#Access-Control-Allow-Headers
		 * - http://enable-cors.org/
		 */
		return HttpResponseBuilder
				.builder(HttpResponseStatus.OK)
				.withHeader("Access-Control-Allow-Origin", "*")
				.withHeader("Access-Control-Allow-Headers", HttpHeaders.Names.AUTHORIZATION + ", X-Oystr-Auth")
				.noCache()
				.build();
	}

	private org.jboss.netty.handler.codec.http.HttpResponse handleGetOrPost(XavanteRequest xeq)
		throws Exception
	{
		ReponseBuilder builder = builderFor(xeq);
		HttpResponse   res     = builder.handle(xeq);
		return toNettyResponse(res);
	}

	private ReponseBuilder builderFor(XavanteRequest xeq)
		throws Exception
	{
		HttpRequest req  = xeq.getRequest();
		Url         url  = xeq.getUrl();
		String      path = url.getPath();
		List<ReponseBuilder> list = buildersByPath.get(path);
		if(list == null)
		{
			return null;
		}

		FluidMap<String> parameters = HttpUtils.parsePostData(req);
		for(ReponseBuilder builder : list)
		{
			FluidMap<String> my = builder.getParameters();
			boolean match = parameters.subsetOf(my);
			if(match)
			{
				return builder;
			}
		}
		return null;
	}
}