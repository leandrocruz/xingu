package br.com.ibnetwork.xingu.utils.html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class HtmlUtils
{
    /*
     * Regexp explanation
     * ANY CHAR '<' SPACES 'TAG' ATTRIBUTES|SPACES '>' ANY CHAR 
     */
    public static Pattern HTML_TAG_PATTERN = Pattern.compile("<\\s*([a-zA-Z0-9]+)\\s*[^>]*>", Pattern.DOTALL);
    
    public static List<ExtractedTag> extractTags(String content)
    {
        //a small optimization
        int start = content.indexOf("<");
        int end = content.indexOf(">");
        if(start < 0 || end < 0 || start > end)
        {
            return null;
        }
        
        Matcher m = HTML_TAG_PATTERN.matcher(content);
        List<ExtractedTag> result = new ArrayList<ExtractedTag>();
        while(m.find())
        {
            String tag = m.group(1);
            result.add(new ExtractedTag(tag, m.start(), m.end()));
        }
        return result;
    }

    public static boolean isHtml(String content)
    {
    	boolean empty = StringUtils.isEmpty(content);
		if(empty || content.indexOf("<") < 0 || content.indexOf(">") < 0)
		{
			return false;
		}
        List<ExtractedTag> tags = extractTags(content);
        for (ExtractedTag tag : tags)
        {
            String tagName = tag.name();
            if("HTML".equalsIgnoreCase(tagName)
                    || "HEAD".equalsIgnoreCase(tagName)
                    || "BODY".equalsIgnoreCase(tagName))
            {
                return true;
            }
        }
        return false;
    }

}
