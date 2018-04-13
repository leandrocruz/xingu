package xingu.http.client.impl.curl;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import xingu.http.client.Attachment;
import xingu.http.client.CommandLineBuilder;
import xingu.http.client.Cookies;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
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
		return new Builder(req, file).build();
	}


	@Override
	public HttpResponse responseFrom(HttpRequest req, File file)
		throws Exception
	{
		String  uri       = req.getUri();
		
		String  proxy     = req.getProxy();
		boolean withProxy = StringUtils.isNotEmpty(proxy);
		
		return CurlResponseParser.responseFrom(uri, file, withProxy);
	}

	@Override
	public String join(List<String> cmd)
	{
		StringBuffer sb = new StringBuffer();
		boolean quote = false;
		for(String s : cmd)
		{
			if(quote)
			{
				sb.append("'").append(s).append("'").append(" ");
				quote = false;
				continue;
			}
			if(quoteNext(s))
			{
				quote = true;
			}
			sb.append(s).append(" ");
		}
		return sb.toString();
	}

	private boolean quoteNext(String s)
	{
		switch(s)
		{
			case "--cookie":
			case "--data":
			case "--data-urlencode":
			case "-H":
				return true;
		}
		return false;
	}

	class Builder
	{
		private HttpRequest req;
		
		private File file;

		private List<String> result = new ArrayList<String>();
		
		public Builder(HttpRequest req, File file)
		{
			this.req = req;
			this.file = file;
		}

		public List<String> build()
			throws Exception
		{
			result.add("curl");
			boolean isPost = req.isPost();
			if(isPost)
			{
				result.add("-X");
				result.add("POST");
			}
			else
			{
				result.add("--get");
			}
			
			String keepAlive = req.getKeepAlive();
			if(StringUtils.isNotEmpty(keepAlive))
			{
				result.add("--keepalive-time");
				result.add(keepAlive);
			}

			result.add("-m");
			result.add("240");
			result.add("-i");
			
			placeSecurityOptions();
			
			result.add("-o"); // output to a file
			result.add(file.toString());

			for(String param : params)
			{
				result.add(param);
			}

			placeAuth();
			placeCertificate();
			placeCookies();
			placeUserAgent();
			placeHeaders();
			placePayload();
			placeProxy();

			boolean isMultipart = req.isMultipart();
			if(isPost && isMultipart)
			{
				placeMultipartFields();
			}
			else
			{
				placeDataFields();
			}

			String uri = req.getUri();
			result.add(uri);
			result.add("--compressed");
			return result;
		}
		
		private void placeSecurityOptions()
		{
			//TODO: use ignoreSSLCertificates() when performing the request
			/*
			if(req.ignoreSSLCertificates())
			{
				result.add("-k"); // ignore server certificate
			}
			*/

			result.add("-k"); // ignore server certificate		
			if(req.sslAllowBeast())
			{
				result.add("--ssl-allow-beast");
			}		
			if(req.sslV3())
			{
				result.add("-3");
			}
		}

		private void placeCertificate()
		{
			String certificate = req.getCertificate();
			if(StringUtils.isNotEmpty(certificate))
			{
				result.add("--cert");
				result.add(certificate);
			}
		}

		private void placeAuth()
		{
			String authUser  = req.getAuthenticationUser();
			String authPwd	 = req.getAuthenticationPassword();
			if(StringUtils.isNotEmpty(authUser) && StringUtils.isNotEmpty(authPwd))
			{
				result.add("--user");
				result.add(authUser + ":" + authPwd);
			}
		}

		private void placeUserAgent()
		{
			String ua = req.getUserAgent();
			if(StringUtils.isNotEmpty(ua))
			{
				result.add("--user-agent");
				result.add(ua);
			}
		}

		private void placeHeaders()
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

		private void placePayload()
		{
			String payload = req.getPayload();
			if(StringUtils.isNotEmpty(payload))
			{
				boolean isSoap = req.isSoap();
				if(isSoap)
				{
					result.add("-d");
				}
				else
				{
					result.add("--data-binary");
				}
				result.add(payload);
			}
		}
		
		private void placeProxy()
		{
			String proxy = req.getProxy();
			
			if(StringUtils.isNotEmpty(proxy))
			{
				result.add("--proxy");
				result.add(proxy);
			}
		}

		private void placeCookies()
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
							|| StringUtils.isEmpty(c.getName()))
					{
						throw new NotImplementedYet("Cookie is null");
					}
					i++;
					String value = c.getValue();
					sb.append(c.getName()).append("=").append(value != null ? value : "");
					
					if(i < len)
					{
						sb.append("; ");
					}
				}
				result.add(sb.toString());
			}
		}
		
		private void placeDataFields()
			throws Exception
		{
			List<NameValue> fields = req.getFields();
			int len = fields == null ? 0 : fields.size();
			if(len > 0)
			{
				StringBuilder dump = new StringBuilder();
				
				//String charset = req.getCharset();
				for(NameValue f : fields)
				{
					String name  = f.getName();
					String value = f.getValue();
					String type  = f.getType();
					
					if(type != null && type.startsWith("dump"))
					{
						value = escapeText(value);
						
						dump.append("--data-urlencode \"")
							.append(name)
							.append("=")
							.append(value)
							.append("\"")
							.append("\n");
					}
					else
					{
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

				dumpParametersToFileIfAny(dump);
			}
		}

		private void placeMultipartFields()
			throws Exception
		{
			StringBuilder dump = new StringBuilder();
			
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
					
					dump
						.append("-F ")
						.append("\"")
						.append(name + "=@" + file.getAbsolutePath()) 
						.append(fileName == null ? "" : ";filename=" + fileName)
						.append(mime == null ? "" : ";type=" + mime)
						.append("\"")
						.append("\n");
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
					dump.append("-F ");
					String name  = f.getName();
					String value = f.getValue();
					String type  = f.getType();
					
					value = escapeText(value);
					
					if(type != null && type.startsWith("enc"))
					{
						int idx = type.indexOf(":");
						String encoding = type.substring(idx + 1);
						if(!"no".equals(encoding))
						{
							value = URLEncoder.encode(value, encoding);
						}
						dump
							.append("\"")
							.append(name + "=" + value)
							.append("\"");
					}
					else if(type != null)
					{
						dump
							.append("\"")
							.append(name + "=" + value + ";type=" + type)
							.append("\"");
					}
					else
					{
						dump
							.append("\"")
							.append(name + "=" + value)
							.append("\"");
					}
					dump.append("\n");
				}
			}
			
			dumpParametersToFileIfAny(dump);
		}
		
		private String escapeText(String text)
		{			
			return text.replace("\"", "\\\"");
		}

		private void dumpParametersToFileIfAny(StringBuilder dump)
			throws Exception
		{
			if(dump.length() > 0)
			{				
				File outputFile = dumpParametersToFile(dump);
				result.add("-K");
				result.add(outputFile.getPath());
			}
		}
		
		private File dumpParametersToFile(StringBuilder dump)
			throws Exception
		{
			File file = null;
			if(req.getContext() != null)
			{
				file = File.createTempFile("dump_curl_", "_file.txt", req.getContext().getRootDirectory());
			}
			else
			{
				file = File.createTempFile("dump_curl_", "_file.txt");
			}		

			FileUtils.write(file, dump);		
			return file;
		}
	}
}