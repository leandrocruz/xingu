package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import br.com.ibnetwork.xingu.utils.html.ExtractedTag;
import br.com.ibnetwork.xingu.utils.html.HtmlUtils;

public class HtmlUtilsTest
{
    private String html = 
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" 
        + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<!-- $Id: index.shtml,v 1.409 2010/11/06 15:30:40 warthog9 Exp $ -->\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
        + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
        + "</head><body><h1>Hello</h1></body></html>";

    
    @Test
    public void testTagRegexp()
        throws Exception
    {
        testPattern("<!DOCTYPE html><h1>", "h1");
        testPattern("<html xmlns=\"http://www.w3.org/1999/xhtml\">", "html");
        testPattern("<a href=\"http://domain.com.br/do?a=b&c=d\">link</a>", "a");
        
        testPattern("<head>", "head");
        testPattern("<head><body>", "head");
        testPattern("<head><body><h1>Hello World</h1></body></head>", "head");
        testPattern("< head >", "head");
        testPattern("junk<head>junk", "head");
        testPattern("junk< head >junk", "head");
        testPattern("<head a='b'>", "head");
        testPattern(" < head a='b'>", "head");
        testPattern("<head a = 'b'>", "head");
        testPattern(" < head a = 'b'>", "head");
        testPattern("<head a=\"b\">", "head");
        testPattern(" < head a=\"b\">", "head");
        testPattern("<head a = \"b\">", "head");
        testPattern(" < head a = \"b\">", "head");
        testPattern("<head>\n", "head");
        
        
        testPattern("<HEAD>", "HEAD");
        testPattern("< HEAD >", "HEAD");
        testPattern("junk<HEAD>junk", "HEAD");
        testPattern("junk< HEAD >junk", "HEAD");
        testPattern("<HEAD a='b'>", "HEAD");
        testPattern(" < HEAD a='b'>", "HEAD");
        testPattern("<HEAD a = 'b'>", "HEAD");
        testPattern(" < HEAD a = 'b'>", "HEAD");
        testPattern("<HEAD a=\"b\">", "HEAD");
        testPattern(" < HEAD a=\"b\">", "HEAD");
        testPattern("<HEAD a = \"b\">", "HEAD");
        testPattern(" < HEAD a = \"b\">", "HEAD");

        Matcher m = HtmlUtils.HTML_TAG_PATTERN.matcher(html);
        assertTrue(m.find());
    }

    private void testPattern(String input, String expected)
    {
        Matcher m = HtmlUtils.HTML_TAG_PATTERN.matcher(input);
        assertTrue(m.find());
        assertEquals(expected, m.group(1));
    }

    @Test
    public void testExtractTags()
        throws Exception
    {
        List<ExtractedTag> tags = HtmlUtils.extractTags(html);
        assertEquals(5, tags.size());
    }

    @Test
    public void testIsHtml()
        throws Exception
    {
        assertTrue(HtmlUtils.isHtml(html));
    }

    @Test
    public void testJson()
        throws Exception
    {
        URL url = Thread.currentThread().getContextClassLoader().getResource("json.txt");
        String json = FileUtils.readFileToString(new File(url.getFile()));
        List<ExtractedTag> tags = HtmlUtils.extractTags(json);
        assertNull(tags);
    }
}
