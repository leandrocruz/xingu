package xingu.url.impl;

import java.util.regex.Matcher;

import xingu.url.DomainName;
import xingu.url.QueryString;
import xingu.url.Url;
import xingu.url.UrlUtils;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class SimpleUrl
	implements Url
{
	private String scheme;

	private String host;

	private int port;

	private String path;

	private String query;

	private QueryString queryString;

	private String fragment;

	private DomainName domainName;

	private String spec;

	SimpleUrl()
	{}

	public SimpleUrl(String spec /* original value */, String scheme, String host, int port, String path, String query,
			String fragment)
	{
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
		this.query = query;
		this.fragment = fragment;
		this.spec = spec;
	}

	@Override
	public boolean isValid()
	{
		return schemeIsValid() && hostIsValid() && portIsValid();
	}

	private boolean portIsValid()
	{
		return port > 0 || port == -1;
	}

	private boolean hostIsValid()
	{
		if (StringUtils.isEmpty(host))
		{
			return false;
		}
		if (isIp())
		{
			return true;
		}
		if ("localhost".equals(host))
		{
			return true;
		}
		return UrlUtils.isDomainValid(host);
	}

	private boolean schemeIsValid()
	{
		if (scheme == null)
		{
			return false;
		}
		return "http".equals(scheme) || "https".equals(scheme);
	}

	@Override
	public String getScheme()
	{
		return scheme;
	}

	@Override
	public String getHost()
	{
		return host;
	}

	@Override
	public DomainName getDomainName()
	{
		if (host == null)
		{
			return null;
		}
		if (domainName == null && !isIp())
		{
			domainName = new DomainNameImpl(host);
		}
		return domainName;
	}

	@Override
	public int getPort()
	{
		return port;
	}

	@Override
	public String getPath()
	{
		return path;
	}

	@Override
	public String getFilename()
	{
		String path = getPath();
		if (path == null)
		{
			return null;
		}
		
		
		Matcher matcher = filenamePattern.matcher(path);
		if (matcher.matches())
		{
			return matcher.group(2);
		}
		return null;
	}

	@Override
	public String getExtension()
	{
		String fileName = getFilename();
		if (fileName == null)
		{
			return null;
		}

		Matcher matcher = extensionPattern.matcher(fileName);
		if (matcher.matches())
		{
			return matcher.group(1);
		}
		return null;
	}

	@Override
	public String getQuery()
	{
		return query;
	}

	@Override
	public QueryString getQueryString()
	{
		if (queryString != null)
		{
			return queryString;
		}

		if (StringUtils.isEmpty(query))
		{
			query = "";
		}

		queryString = new QueryStringImpl(query);
		return queryString;
	}

	private String value(String name)
	{
		QueryString q = getQueryString();
		if (q == null)
		{
			return null;
		}
		return q.get(name);
	}

	@Override
	public boolean hasParam(String name)
	{
		return value(name) != null;
	}

	@Override
	public boolean hasParam(String name, String value)
	{
		String v = value(name);
		return v == null ? value == null : v.equals(value);
	}

	@Override
	public Url addParam(String name, String value)
	{
		QueryString q = getQueryString();
		if (q == null)
		{
			queryString = new QueryStringImpl();
			q = queryString;
		}
		q.add(name, value);
		return this;
	}

	@Override
	public String getFragment()
	{
		return fragment;
	}

	@Override
	public boolean isFavIcon()
	{
		return /*
				 * query == null && fragment == null &&
				 */"/favicon.ico".equalsIgnoreCase(path);
	}

	@Override
	public boolean isIp()
	{
		if (host == null)
		{
			return false;
		}
		String[] parts = host.split("\\.");
		if (parts.length != 4)
		{
			return false;
		}
		try
		{
			for (String part : parts)
			{
				Integer.parseInt(part);
			}
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getScheme()).append("://").append(getHost());

		if (port > 0)
		{
			sb.append(":").append(port);
		}

		String pqsf = getPathQueryStringAndFragment();
		if(StringUtils.isNotEmpty(pqsf))
		{
			sb.append(pqsf);
		}

		String result = sb.toString();
		return result;
	}

	@Override
	public String getPathQueryStringAndFragment()
	{
		StringBuffer sb = new StringBuffer();
		String path = getPath();
		if (path != null && !"/".equals(path))
		{
			sb.append(path);
		}

		QueryString q = getQueryString();
		if (q != null)
		{
			String value = q.toString();
			if (StringUtils.isNotEmpty(value))
			{
				sb.append("?");
				sb.append(value);
			}
		}

		String fragment = getFragment();
		if (fragment != null)
		{
			sb.append("#");
			sb.append(fragment);
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Url))
			return false;
		Url other = (Url) obj;
		return safeEquals(scheme, other.getScheme()) && safeEquals(host, other.getHost()) && port == other.getPort()
				&& comparePaths(path, other.getPath()) && safeEquals(query, other.getQuery())
				&& safeEquals(fragment, other.getFragment());
	}

	private boolean comparePaths(String path, String otherPath)
	{
		String first = forceSlashPath(path);
		String second = forceSlashPath(otherPath);
		return first.equals(second);
	}

	private boolean safeEquals(String a, String b)
	{
		return a == null ? b == null : a.equals(b);
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public String getSpec()
	{
		// TODO: remove this method after UpdateSimpleUrl is run
		return spec;
	}

	@Override
	public boolean isSameDomainAndPath(Url other)
	{
		return getDomainAndPath().equals(other.getDomainAndPath());
	}

	@Override
	public String getDomainAndPath()
	{
		String address = isIp() ? getHost() : getDomainName().fullName();
		String path = getPath();
		if (path != null)
		{
			address += path;
		}
		return address;
	}

	/**
	 * Check if this address applies to another given address.
	 * <p>
	 * It does apply if the domain name is the same or if it's a 'super' domain,
	 * <b>and</b> if the path is the same or is a 'super' path.
	 * <p>
	 * For example, the address <code>kidux.net</code> <b>does</b> apply to
	 * <code>www.kidux.net/kastle</code>. On the other hand,
	 * <code>www.kidux.net/kastle</code> does <b>not</b> apply to
	 * <code>kidux.net</code>.
	 */
	@Override
	public boolean appliesTo(Url url)
	{
		if (!matchesDomain(url))
		{
			return false;
		}
		if (pathLevel() == 0)
		{
			return true;
		}
		return matchesPath(url);
	}

	private boolean matchesDomain(Url url)
	{
		if (url.domainLevel() < domainLevel())
		{
			return false;
		}

		String fullName = getDomainName().fullName();
		String otherFullName = url.getDomainName().fullName();
		if (!otherFullName.equals(fullName) && !otherFullName.endsWith("." + fullName))
		{
			return false;
		}
		return true;
	}

	private boolean matchesPath(Url other)
	{
		if (other.pathLevel() < pathLevel())
		{
			return false;
		}
		String otherPath = forceSlashPath(other.getPath());
		String thisPath = forceSlashPath(getPath());
		if (!otherPath.startsWith(thisPath))
		{
			return false;
		}
		return true;
	}

	private String forceSlashPath(String otherPath)
	{
		return otherPath != null && otherPath.length() > 0 ? otherPath : "/";
	}

	private transient Integer domainLevel;

	private transient Integer pathLevel;

	public int domainLevel()
	{
		String domain = getDomainName() != null ? getDomainName().fullName() : null;
		if (domainLevel == null)
		{
			domainLevel = count('.', domain);
		}
		return domainLevel;
	}

	public int pathLevel()
	{
		if (pathLevel == null)
		{
			String path2 = getPath();
			pathLevel = count('/', forceSlashPath(path2));
		}
		return pathLevel;
	}

	private int count(char character, String text)
	{
		if (text == null)
		{
			return 0;
		}
		int start = text.indexOf(character);
		if (start == -1)
		{
			return 0;
		}
		int end = text.lastIndexOf(character);
		char[] allCharacters = text.toCharArray();
		int count = 0;
		for (int index = start; index <= end; index++)
		{
			if (allCharacters[index] == character)
			{
				count++;
			}
		}
		return count;
	}
}
