package xingu.http.client.impl.curl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import xingu.http.client.Cookies;
import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;
import xingu.http.client.impl.CommandLineBuilderSupport;
import xingu.netty.http.HttpUtils;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

/*
 * See: http://curl.haxx.se/docs/manpage.html
 */
public class CurlCommandLineBuilder
	extends CommandLineBuilderSupport
	implements Configurable
{
	List<String> params = new ArrayList<String>();
	
	@Override
	public String name()
	{
		return "curl";
	}

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		Configuration[] params = conf.getChild("params").getChildren("param");
		for(Configuration param : params)
		{
			String value = param.getAttribute("value");
			this.params.add(value);
		}
	}

	@Override
	public List<String> buildLine(HttpRequest req, File file) 
		throws Exception
	{
		List<String> result = new ArrayList<String>();
		result.add("curl");
		result.add("-i");
		result.add("-k"); // ignore server certificate
		result.add("-o"); // output to a file
		result.add(file.toString());

		for(String param : params)
		{
			result.add(param);
		}

		String authUser  = req.getAuthenticationUser();
		String authPwd	 = req.getAuthenticationPassword();
		if(StringUtils.isNotEmpty(authUser) && StringUtils.isNotEmpty(authPwd))
		{
			result.add("--user");
			result.add(authUser + ":" + authPwd);
		}
		
		String certificate = req.getCertificate();
		if(StringUtils.isNotEmpty(certificate))
		{
			result.add("--cert");
			result.add(certificate);
		}

		placeCookies(req, 	result);
		placeUserAgent(req, result);
		placeHeaders(req, 	result);

		if(req.isPost())
		{
			placePostFields(req, result);			
		}
		else
		{
			placeQueryStringFields(req, result);
		}
		
		result.add("--compressed");
		
		return result;
	}

	private void placePostFields(HttpRequest req, List<String> result)
		throws Exception
	{
		if(req.isMultipart())
		{
			placeMultipartFields(req, result);
		}
		else
		{
			placeDataFields(req, result);
		}
		result.add(req.getUri());
	}

	private void placeMultipartFields(HttpRequest req, List<String> result)
		throws Exception
	{
		// TODO: try to work with only one list
		List<NameValue> uploadFields = req.getAttachments();
		int len = uploadFields == null ? 0 : uploadFields.size();
		if(len > 0)
		{
			for(NameValue f : uploadFields)
			{
				result.add("-F");
				result.add(f.getName() + "=@" + f.getValue());
			}
		}

		List<NameValue> fields = req.getFields();
		int len2 = fields == null ? 0 : fields.size();
		if(len2 > 0)
		{
			for(NameValue f : fields)
			{
				result.add("-F");
				String name  = f.getName();
				String value = f.getValue();
				String type  = f.getContentType();
				if(type == null)
				{
					result.add(name + "=" + value);
				}
				else
				{
					result.add(name + "=" + value + ";type=" + type);	
				}
				// value = URLEncoder.encode(value, charset);
				
			}
		}
	}

	private void placeUserAgent(HttpRequest req, List<String> result)
	{
		String ua = req.getUserAgent();
		if(StringUtils.isNotEmpty(ua))
		{
			result.add("--user-agent");
			result.add(ua);
		}
	}

	private void placeHeaders(HttpRequest req, List<String> result)
	{
		List<NameValue> fields = req.getHeaders();
		int len = fields == null ? 0 : fields.size();
		if(len > 0)
		{
			for(NameValue f : fields)
			{
				result.add("-H");
				String name  = f.getName();
				String value = f.getValue();
				result.add(name + ": " + value);
				if(name.startsWith(HttpHeaders.Names.CONTENT_TYPE))
				{
					String charset = HttpUtils.charset(value, HttpUtils.DEFAULT_HTTP_CHARSET_NAME);
					req.setCharset(charset);
				}
			}
		}
	}

	private void placeQueryStringFields(HttpRequest req, List<String> result) 
		throws UnsupportedEncodingException
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();
		String uri = req.getUri();
		
		if(len == 0)
		{
			result.add(uri);
			return;
		}
		
		String charset = HttpUtils.DEFAULT_HTTP_CHARSET.name();
		StringBuffer buffer = new StringBuffer(uri);
		int i = 0;

		if(buffer.indexOf("?") > 0)
		{
			buffer.append("&");
		}
		else
		{
			buffer.append("?");
		}
		
		for(NameValue field : fields)
		{
			i++;
			String name    = field.getName();
			String value   = field.getValue();
			String encoded = URLEncoder.encode(value, charset);
			
			buffer
				.append(name)
				.append("=")
				.append(encoded);
			
			if(i < len)
			{
				buffer.append("&");
			}
		}

		String txt = buffer.toString();
		result.add(txt);
	}

	private void placeDataFields(HttpRequest req, List<String> result)
		throws Exception
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();		

		if(len > 0)
		{
			int i = 0;
			String charset = req.getCharset();
			result.add("--data");
			StringBuffer sb = new StringBuffer();
			for(NameValue f : fields)
			{
				i++;
				String name  = f.getName();
				String value = f.getValue();
				value = URLEncoder.encode(value, charset);
				sb.append(name).append("=").append(value);
				if(i < len)
				{
					sb.append("&");
				}
			}
			result.add(sb.toString());
		}
	}

	private void placeCookies(HttpRequest req, List<String> result)
	{
		Cookies cookies = req.getCookies();
		int len = cookies == null ? 0 : cookies.size();
		if(len > 0)
		{
			int i = 0;
			result.add("--cookie");
			StringBuffer sb = new StringBuffer();
			for(Cookie c : cookies.getBuffer())
			{
				if(c == null 
						|| StringUtils.isEmpty(c.getName())
						|| StringUtils.isEmpty(c.getValue()))
				{
					throw new NotImplementedYet("Cookie is null");
				}
				i++;
				sb.append(c.getName()).append("=").append(c.getValue());
				
				if(i < len)
				{
					sb.append("; ");
				}
			}
			result.add(sb.toString());
		}
	}
}