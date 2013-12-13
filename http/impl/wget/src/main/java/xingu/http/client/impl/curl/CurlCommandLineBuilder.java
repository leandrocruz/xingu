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
	public String buildLine(HttpRequest req, File file)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("curl -i -o ").append(file);
		
		String certificate = req.getCertificate();
		if(StringUtils.isNotEmpty(certificate))
		{
			buffer.append(" --cert ").append(certificate);
		}
		
		List<Cookie> cookies = req.getCookies();
		if(cookies != null && cookies.size() > 0)
		{
			for(Cookie c : cookies)
			{
				buffer
					.append(" --header 'Cookie: ")
					.append(c.getName())
					.append("=")
					.append(c.getValue())
					.append("'");
			}
		}
		
		List<NameValue> fields = req.getFields();
		if(fields != null && fields.size() > 0)
		{
			boolean isPost = req.isPost();
			if(isPost)
			{
				buffer.append(" --data '");
				int len = fields.size();
				int i = 0;
				for(NameValue f : fields)
				{
					i++;
					buffer.append(f.getName()).append("=").append(f.getValue());
					if(i < len)
					{
						buffer.append("&");
					}
				}
				buffer.append("'");
			}
			else
			{
				throw new NotImplementedYet();
			}
		}

		buffer.append(" ").append(req.getUri());
		return buffer.toString();
	}
}