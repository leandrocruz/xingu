package xingu.url;

import java.util.Comparator;

import xingu.url.DomainName;
import xingu.url.Url;

public class UrlComparator
    implements Comparator<Url>
{
    @Override
    public int compare(Url u1, Url u2)
    {    
        int d1 = domainLevel(u1);
        int d2 = domainLevel(u2);
    
        if (d1 == d2)
        {
            int p1 = pathLevel(u1);
            int p2 = pathLevel(u2);
            int result = p2 - p1;
            return result != 0 ? result : stringCompare(u1, u2);
        }
    
        int result = d2 - d1;
        return result != 0 ? result : stringCompare(u1, u2);
    }
    
    private int stringCompare(Url u1, Url u2)
    {
        String s1 = u1.toString();
        String s2 = u2.toString();
        return s1.compareToIgnoreCase(s2);
    }
    
    private int domainLevel(Url url)
    {
        DomainName dn = url.getDomainName();
        if(dn == null)
        {
            return 0;
        }
        String domain = dn.fullName();
        int level = count('.', domain);
        return level;
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
    
    private int pathLevel(Url url)
    {
        String path = url.getPath();
        int level = count('/', path);
        return level;
    }    
}
