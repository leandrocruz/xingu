package xingu.http.client.impl.curl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;
import xingu.http.client.impl.CommandLineBuilderSupport;

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

		if(req.isPost())
		{
			placePostFields(req, buffer);
		}
		else
		{
			placeQueryStringFields(req, buffer);
		}

		buffer.append(" --compressed");

		return buffer.toString();
	}

	private void placePostFields(HttpRequest req, StringBuffer buffer)
	{
		if(req.isMultipart())
		{
			placeMultipartFields(req, buffer);
		}
		else
		{
			placeDataFields(req, buffer);
		}
		buffer.append(" '").append(req.getUri()).append("'");
	}

	private void placeMultipartFields(HttpRequest req, StringBuffer buffer)
	{
		// TODO: try to work with only one list
		List<NameValue> uploadFields = req.getUploadFiles();
		int len = uploadFields == null ? 0 : uploadFields.size();

		List<NameValue> fields = req.getFields();
		int len2 = fields == null ? 0 : fields.size();

		if(len > 0)
		{
			for(NameValue f : uploadFields)
			{
				buffer.append(" -F ");
				buffer.append(f.getName()).append("=@'").append(f.getValue()).append("'");
			}
		}

		if(len2 > 0)
		{
			for(NameValue f : fields)
			{
				buffer.append(" -F ");
				buffer.append(f.getName()).append("='").append(f.getValue()).append("'");
			}
		}
	}

	private void placeUserAgent(HttpRequest req, StringBuffer buffer)
	{
		String ua = req.getUserAgent();
		if(StringUtils.isNotEmpty(ua))
		{
			buffer.append(" --user-agent '").append(ua).append("'");
		}
	}

	private void placeHeaders(HttpRequest req, StringBuffer buffer)
	{
		List<NameValue> fields = req.getHeaders();
		int len = fields == null ? 0 : fields.size();
		if(len > 0)
		{
			for(NameValue f : fields)
			{
				buffer.append(" -H '");
				buffer.append(f.getName()).append(": ").append(f.getValue());
				buffer.append("'");
			}
		}
	}

	private void placeQueryStringFields(HttpRequest req, StringBuffer buffer)
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();
		StringBuffer uri = new StringBuffer(req.getUri());

		if(len > 0)
		{
			int i = 0;
			uri.append("?");
			for(NameValue f : fields)
			{
				i++;
				uri.append(f.getName()).append("=").append(f.getValue());
				if(i < len)
				{
					uri.append("&");
				}
			}
		}
		buffer.append(" '").append(uri).append("'");
	}

	private void placeDataFields(HttpRequest req, StringBuffer buffer)
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();

		if(len > 0)
		{
			int i = 0;
			buffer.append(" --data '");
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
	}

	private void placeCookies(HttpRequest req, StringBuffer buffer)
	{
		List<Cookie> cookies = req.getCookies();
		int len = cookies == null ? 0 : cookies.size();
		if(len > 0)
		{
			int i = 0;
			buffer.append(" --cookie '");

			for(Cookie c : cookies)
			{
				i++;
				buffer.append(c.getName()).append("=").append(c.getValue());
				if(i < len)
				{
					buffer.append("; ");
				}
			}
			buffer.append("'");
		}
	}
}