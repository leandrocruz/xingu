package xingu.http.client.impl.curl;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import xingu.http.client.Attachment;
import xingu.http.client.Cookies;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.http.client.impl.CommandLineBuilder;
import xingu.lang.NotImplementedYet;
import xingu.netty.http.HttpUtils;

/*
 * See: http://curl.haxx.se/docs/manpage.html
 */
public class CurlCommandLineBuilder
	implements CommandLineBuilder, Configurable
{
	private List<String> params = new ArrayList<String>();
	
	private boolean encodeParameterNames;
	
	@Override
	public String name()
	{
		return "curl";
	}

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		encodeParameterNames = conf.getChild("params").getAttributeAsBoolean("encodeParameterNames", false);
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
		
		result.add("-X");
		boolean isPost = req.isPost();
		if(isPost)
		{
			result.add("POST");
		}
		else
		{
			result.add("GET");
		}

		result.add("-m");
		result.add("60");
		result.add("-i");
		result.add("-k"); // ignore server certificate //TODO: use ignoreSSLCertificates()
		result.add("-o"); // output to a file
		result.add(file.toString());

		for(String param : params)
		{
			result.add(param);
		}

		placeAuth		(req, result);
		placeCertificate(req, result);
		placeCookies	(req, result);
		placeUserAgent	(req, result);
		placeHeaders	(req, result);
		placePayload	(req, result);

		boolean isMultipart = req.isMultipart();
		if(isPost && isMultipart)
		{
			placeMultipartFields(req, result);
		}
		else
		{
			placeDataFields(req, result);
		}

		String uri = req.getUri();
		result.add(uri);

		result.add("--compressed");

		return result;
	}

	private void placeDataFields(HttpRequest req, List<String> result)
		throws Exception
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();		

		if(len > 0)
		{
			//String charset = req.getCharset();
			for(NameValue f : fields)
			{
				String name  = f.getName();
				String value = f.getValue();
				String type  = f.getType();
				if(type != null && type.startsWith("enc"))
				{
					int idx = type.indexOf(":");
					String encoding = type.substring(idx + 1);
					if(!"no".equals(encoding))
					{
						if(encodeParameterNames)
						{
							name  = URLEncoder.encode(name, encoding);
						}
						value = URLEncoder.encode(value, encoding);
					}
					result.add("--data");
				}
				else
				{
					result.add("--data-urlencode");
				}
				result.add(name+"="+value);
			}
		}
	}

	private void placeMultipartFields(HttpRequest req, List<String> result)
		throws Exception
	{
		List<Attachment> attachments = req.getAttachments();
		int len = attachments == null ? 0 : attachments.size();
		if(len > 0)
		{
			for(Attachment attachment : attachments)
			{
				String name     = attachment.getName();
				String fileName = attachment.getFileName();
				File   file     = attachment.getFile();
				String mime     = attachment.getMime();
				result.add("-F");
				result.add(name + "=@" + file.getAbsolutePath() 
						+ (fileName == null ? "" : ";filename=" + fileName)
						+ (mime == null ? "" : ";type=" + mime));
			}
		}

		/*
		 * See:
		 * - http://www.ietf.org/rfc/rfc2388.txt
		 * - http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.1
		 * - http://curl.haxx.se/docs/manpage.html#-F
		 */
		List<NameValue> fields = req.getFields();
		int len2 = fields == null ? 0 : fields.size();
		if(len2 > 0)
		{
			for(NameValue f : fields)
			{
				result.add("-F");
				String name  = f.getName();
				String value = f.getValue();
				String type  = f.getType();
				if(type != null && type.startsWith("enc"))
				{
					int idx = type.indexOf(":");
					String encoding = type.substring(idx + 1);
					if(!"no".equals(encoding))
					{
						value = URLEncoder.encode(value, encoding);
					}
					result.add(name + "=" + value);
				}
				else if(type != null)
				{
					result.add(name + "=" + value + ";type=" + type);
				}
				else
				{
					result.add(name + "=" + value);
				}
			}
		}
	}

	private void placeCertificate(HttpRequest req, List<String> result)
	{
		String certificate = req.getCertificate();
		if(StringUtils.isNotEmpty(certificate))
		{
			result.add("--cert");
			result.add(certificate);
		}
	}

	private void placeAuth(HttpRequest req, List<String> result)
	{
		String authUser  = req.getAuthenticationUser();
		String authPwd	 = req.getAuthenticationPassword();
		if(StringUtils.isNotEmpty(authUser) && StringUtils.isNotEmpty(authPwd))
		{
			result.add("--user");
			result.add(authUser + ":" + authPwd);
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

	private void placePayload(HttpRequest req, List<String> result)
	{
		String payload = req.getPayload();
		if(StringUtils.isNotEmpty(payload))
		{
			result.add("--data-binary");
			result.add("\"" + payload + "\"");
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

	@Override
	public HttpResponse responseFrom(HttpRequest req, File file)
		throws Exception
	{
		String uri = req.getUri();
		return CurlResponseParser.responseFrom(uri, file);
	}
}