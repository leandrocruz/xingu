package xingu.url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xingu.url.DomainName;
import xingu.url.Url;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.StringUtils;


public class UrlUtils
{
    public static final String SCHEME_REGEX = "[A-Za-z]+";
    
    public static final String USER_AND_PASS = "[A-Za-z0-9_-]+(?::[^@]+)?@";
    
    public static final String IP = "\\d+\\.\\d+\\.\\d+\\.\\d+";
    
    public static final String HOSTNAME = "[A-Za-z0-9_-]+";
    
    public static final String DOMAIN = "(?:[a-zA-Z0-9_-]+\\.)+[a-zA-Z0-9-]+";
    
    public static final String HOST = "("+IP+")|(?:"+HOSTNAME+")|("+DOMAIN+")";
    
    public static final String PORT = ":(\\d+)";
    
    public static final String PATH = "(?:/[^ /?#]*)+";
    
    public static final String QUERYSTRING = "\\?([^#]*)";
    
    public static final String ANCHOR = "#(.*)";

    //foo://username:password@example.com:8042/over/there/index.dtb?type=animal&name=narwhal#nose
    //http://en.wikipedia.org/wiki/URI_scheme
    public static final String R = "^(?:(http|https)://)?([^\\s])+$";
    
    public static final String REGEX = "^("+SCHEME_REGEX+"://)?(()@()) ("+USER_AND_PASS+")?("+HOST+")(?:"+PORT+")?("+PATH+")?(?:"+QUERYSTRING+")?(?:"+ANCHOR+")?$";
    
    public static Pattern pattern = Pattern.compile(R);
    
    /*
     * http://answers.oreilly.com/topic/280-how-to-validate-urls-with-regular-expressions/
     */
    public static final Pattern Domain = Pattern.compile("[a-zA-Z0-9]+(\\.[a-zA-Z0-9\\-]+)+");
    
    
    private static String host(Url url)
    {
        String host = url.getHost();
        if(host != null)
        {
            return host;
        }
        String spec = url.getSpec();
        if(spec != null)
        {
            Url other = UrlParser.parse(spec);
            host = other.getHost();
        }
        if(host != null)
        {
            return host;
        }
        throw new NotImplementedYet("Host not found on "+url);
    }
    
    public static String domainFrom(Url url)
    {
        DomainName domainName = url.getDomainName();
        if(domainName != null)
        {
            return domainName.registeredName();
        }
        return host(url);
    }
    
    public static String siteFrom(Url url)
    {
        String host = host(url);
        if(host.startsWith("www."))
        {
            return host.substring("www.".length());
        }
        return host;
    }
    
    public static boolean isHostOnly(Url url)
    {
        String path = url.getPath();
        boolean noPath = path == null || path.equals("/");
        boolean noQueryString = url.getQueryString() == null;
        return noPath && noQueryString;
    }
    
    public static boolean isMain(Url url, boolean wwwRelevant)
    {
        if(!wwwRelevant)
        {
            DomainName domainName = url.getDomainName();
            if (domainName == null)
            {
                return false;
            }
            String registered = domainName.registeredName();
            String site = siteFrom(url);
            boolean isHost = isHostOnly(url);
            return registered.equals(site) && isHost;
        }
        return false;
    }    
    
    public static boolean contains(Url url1, Url url2)
    {
        String u1 = url1.getHost();
        String u2 =  url2.toString();
        if(url2.toString().startsWith("http://"))
        {
            u2 = u2.toString().replaceFirst("http://", "");
        }
        if(u1.endsWith(u2))
        {
            return true;
        }
        return false;
    }    

    public static boolean isSameDomain(Url url1, Url url2, boolean wwwRelevant)
    {
        String u1 = url1.getHost();
        String u2 = url2.getHost();
        
        DomainName d1 = url1.getDomainName();
        DomainName d2 = url2.getDomainName();
        
        if(!(wwwRelevant || d1 == null || d2 == null))
        {
            String rn1 = d1.registeredName();
            String rn2 = d2.registeredName();
            
            if(contains(url1, url2))
            {
                return true;
            }
            
            if(rn2.equals(siteFrom(url2)))
            {                
                return rn1.equals(rn2);
            }
        }
        return u1.equals(u2);
    }

    public static boolean isDomainValid(String host)
    {
        Matcher m = Domain.matcher(host);
        boolean valid = m.matches();
        return valid;
    }
    
    public static final Url init(Url url)
    {
        String scheme = url.getScheme();
        String host = url.getHost();
        if(StringUtils.isEmpty(scheme) || StringUtils.isEmpty(host))
        {
            String spec = url.getSpec();
            return UrlParser.parse(spec);
        }
        return url;
    }
}
