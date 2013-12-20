package xingu.http.client.impl.curl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;
import xingu.http.client.impl.CommandLineBuilderSupport;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

/*
 * See: http://curl.haxx.se/docs/manpage.html
 */
public class CurlCommandLineBuilder
	extends CommandLineBuilderSupport
{
	@Override
	public String name()
	{
		return "curl";
	}

	@Override
	public String buildLine(HttpRequest req, File file)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("curl -v -i -o ").append(file);
		
		String certificate = req.getCertificate();
		if(StringUtils.isNotEmpty(certificate))
		{
			buffer.append(" --cert ").append(certificate);
			String certificatePwd = req.getCertificatePassword();
			if(StringUtils.isNotEmpty(certificatePwd))
			{
				buffer.append(":").append(certificatePwd).append(" ");
			}
		}
		
		placeCookies(req, buffer);
		placeUserAgent(req, buffer);
		placeHeaders(req, buffer);
		placeFields(req, buffer);

		buffer.append(" \"").append(req.getUri()).append("\" --compressed");
		return buffer.toString();
	}
	
	private void placeUserAgent(HttpRequest req, StringBuffer buffer)
	{
		buffer.append(" --user-agent '").append(req.getUserAgent()).append("'");
	}

	private void placeHeaders(HttpRequest req, StringBuffer buffer)
	{
		List<NameValue> fields = req.getHeaders();
		int len = fields == null ? 0 : fields.size();
		if(len > 0)
		{
			for(NameValue f : fields)
			{
				buffer.append(" -H \"");
				buffer.append(f.getName()).append(": ").append(f.getValue());
				buffer.append("\"");
			}
		}
	}

	private void placeFields(HttpRequest req, StringBuffer buffer)
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();
		if(len > 0)
		{
			boolean isPost = req.isPost();
			if(isPost)
			{
				int i = 0;
				buffer.append(" --data \"");
				for(NameValue f : fields)
				{
					i++;
					buffer.append(f.getName()).append("=").append(f.getValue());
					if(i < len)
					{
						buffer.append("&");
					}
				}
				buffer.append("\"");
			}
			else
			{
				throw new NotImplementedYet();
			}
		}
	}

	private void placeCookies(HttpRequest req, StringBuffer buffer)
	{
		List<Cookie> cookies = req.getCookies();
		int len = cookies == null ? 0 : cookies.size();
		if(len > 0)
		{
			int i = 0;
			buffer.append(" --cookie \"");
			for(Cookie c : cookies)
			{
				i++;
				buffer.append(c.getName()).append("=").append(c.getValue());
				if(i < len)
				{
					buffer.append("; ");
				}
			}
			buffer.append("\"");
		}
	}
}