package xingu.url;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.ibnetwork.xingu.lang.BadParameter;

public class Url2Test
{
    @Test
    public void testDomainAppliesToSubdomain()
    {
        Url domain = urlFrom("kidux.net");
        Url subdomain = urlFrom("http://www.kidux.net");
        assertTrue(domain.appliesTo(subdomain));
    }
    
    @Test
    public void testSubdomainDoesNotApplyToDomain()
    {
        Url domain = urlFrom("kidux.net");
        Url subdomain = urlFrom("http://www.kidux.net");
        assertFalse(subdomain.appliesTo(domain));
    }
    
    @Test
    public void testRootPathAppliesToRootPathWithoutSlashAndViceVersa()
    {
        Url withSlash = urlFrom("kidux.net/");
        Url noSlash = urlFrom("http://kidux.net");
        assertTrue(withSlash.appliesTo(noSlash));
        assertTrue(noSlash.appliesTo(withSlash));
    }
    
    @Test
    public void testDirectoryAppliesToTheSameDirectory()
    {
        Url directory = urlFrom("kidux.net/windows/");
        Url sameDirectory = urlFrom("http://kidux.net/windows/");
        assertTrue(directory.appliesTo(sameDirectory));
        assertTrue(sameDirectory.appliesTo(directory));
    }
    
    @Test
    public void testDirectoryAppliesToSubdirectory()
    {
        Url directory = urlFrom("kidux.net/windows/");
        Url subdirectory = urlFrom("http://kidux.net/windows/latest/");
        assertTrue(directory.appliesTo(subdirectory));
    }
    
    @Test
    public void testSubdirectoryDoesNotApplyToDirectory()
    {
    	Url directory = urlFrom("kidux.net/windows/");
    	Url subdirectory = urlFrom("http://www.kidux.net/windows/latest/");
        assertFalse(subdirectory.appliesTo(directory));
    }
    
    @Test
    public void testDirectoryAppliesToSubdirectoryInSuperDomain()
    {
    	Url directory = urlFrom("subdomain.kidux.net/windows/");
    	Url subdirectory = urlFrom("http://kidux.net/windows/latest/");
        assertFalse(directory.appliesTo(subdirectory));
    }

    @Test
    public void testFileAppliesToSameFile()
    {
    	Url oneAddress = urlFrom("download.kidux.net/windows/download");
    	Url anotherAddress = urlFrom("http://download.kidux.net/windows/download?id=123");
        assertTrue(oneAddress.appliesTo(anotherAddress));
        assertTrue(anotherAddress.appliesTo(oneAddress));
    }

    @Test
    public void testFileDoesNotApplyToDifferentFile()
    {
        Url oneAddress = urlFrom("download.kidux.net/windows/download");
        Url anotherAddress = urlFrom("http://download.kidux.net/windows/about");
        assertFalse(oneAddress.appliesTo(anotherAddress));
        assertFalse(anotherAddress.appliesTo(oneAddress));
    }

    @Test
    public void testGoodUrls()
    {
    	urlFrom("foo");
    	urlFrom("foo/");
    	urlFrom("foo.com");
    	urlFrom("foo.com/");
    	urlFrom("http://localhost");
    	urlFrom("http://jaspion:8181");
    	urlFrom("http://jaspion/");
    	urlFrom("http://abc.com");
    	urlFrom("http://user_name-1@abc.com");
    	urlFrom("http://user_name-1:pass_word-1@abc.com");
    	urlFrom("http://user:pass word@abc.com");
    	urlFrom("http://abc.com/stuff?x=foo&y=bar&phrase=hello%20world");
    	urlFrom("http://abc.com/stuff?x=foo&y=bar&phrase=hello%20world#foo");
    	urlFrom("https://mail.google.com/mail/help/images/icons/storage_foo.gif");
    	urlFrom("ftp://dummy.net");
    }

    @Test
    public void testBadUrls()
    {
    	String urls[] = { "foo bar", ":foo.com", "http://user_name-1:abc.com" };
    	for (String url : urls)
		{
    		try
			{
				urlFrom(url);
			}
			catch (BadParameter e)
			{
				continue;
			}
    		fail("Invalid url (" + url + ") not detected.");
		}

    	// As URLs a seguir ainda não são detectadas como invalidas
		// "http://@abc.com", "http:/foo.com/", "/foo.com", "http//foo.com/",
    	// "file://c:/documents/schoolwork.doc", "file:///c:/documents/schoolwork.doc"
    }

    private Url urlFrom(String url)
    {
        return UrlParser.parse(url);
    }
}