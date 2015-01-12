package xingu.url;

import java.net.URI;

import xingu.url.Url;
import xingu.url.UrlParser;
import xingu.utils.StringUtils;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;


/*
 * See:
 * 
 * - http://blog.lunatech.com/2009/02/03/what-every-web-developer-must-know-about-url-encoding
 * - http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/net/UrlEscapers.html
 */
public class UrlEncoder
{
	private static Escaper	pathEscaper		= UrlEscapers.urlPathSegmentEscaper();

	private static Escaper	fragmentEscaper	= UrlEscapers.urlFragmentEscaper();
			
	public static String encode(String spec)
		throws Exception
	{
		Url    url      = UrlParser.parse(spec);
		String scheme   = url.getScheme();
		String auth     = null;
		String host     = url.getHost();
		int    port     = url.getPort();
		String path     = url.getPath();
		String query    = url.getQuery();
		String fragment = url.getFragment();
		URI    uri      = new URI(scheme, auth, host, port, path, query, fragment);
		return uri.toString();
	}

	public static final String encodeGuava(String spec)
		throws Exception
	{
		Url    url      = UrlParser.parse(spec);
		String scheme   = url.getScheme();
		String auth     = null;
		String host     = url.getHost();
		int    port     = url.getPort();
		String path     = url.getPath();
		String query    = url.getQuery();
		String fragment = url.getFragment();

		StringBuffer sb = new StringBuffer();
		sb.append(scheme).append("://").append(host);

		if (port > 0)
		{
			sb.append(":").append(port);
		}

		if(StringUtils.isNotEmpty(path))
		{
			String[] parts = path.split("/");
			for(String part : parts)
			{
				if(StringUtils.isNotEmpty(part))
				{
					String escaped = pathEscaper.escape(part);
					sb.append("/").append(escaped);
				}
			}
		}

		if (StringUtils.isNotEmpty(query))
		{
			sb.append("?");
			sb.append(query);
		}

		if (StringUtils.isNotEmpty(fragment))
		{
			String escaped = fragmentEscaper.escape(fragment);
			sb.append("#").append(escaped);
		}
		
		String result = sb.toString();
		return result;
	}
}
