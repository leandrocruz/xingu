package xingu.utils;

import static org.junit.Assert.assertEquals;

import java.text.Collator;
import java.util.Locale;

import org.junit.Test;

import xingu.utils.StringUtils;

public class StringUtilsTest
{
	@Test
	public void testUpperCaseStart()
	{
		assertEquals(false, StringUtils.upperCaseStart(""));
		assertEquals(false, StringUtils.upperCaseStart(" "));
		assertEquals(false, StringUtils.upperCaseStart("aa"));
		assertEquals(false, StringUtils.upperCaseStart("aA"));
		assertEquals(true, StringUtils.upperCaseStart("Aa"));
		assertEquals(true, StringUtils.upperCaseStart("\u0041")); // "A"
		assertEquals(false, StringUtils.upperCaseStart("\u0061")); // "a"
	}

	@Test
	public void testNormalizeWhiteSpace()
	{
		String noBreakSpace = "\u00A0";
		assertEquals("", StringUtils.normalizeWhiteSpace(""));
		assertEquals("", StringUtils.normalizeWhiteSpace(" "));
		assertEquals("", StringUtils.normalizeWhiteSpace("\n\t "));
		assertEquals("", StringUtils.normalizeWhiteSpace(noBreakSpace));
		assertEquals("start end", StringUtils.normalizeWhiteSpace("start"+noBreakSpace+"end"));
		assertEquals("start end", StringUtils.normalizeWhiteSpace("\n\t"+noBreakSpace+"start"+noBreakSpace+"end"+noBreakSpace));
		assertEquals("Sentença é!", StringUtils.normalizeWhiteSpace("Sentença é!"));
	}

	@Test
	public void testFlat()
	{
		String noBreakSpace = "\u00A0";
		assertEquals("", StringUtils.flat(""));
		assertEquals("", StringUtils.flat(" "));
		assertEquals("", StringUtils.flat(noBreakSpace));
		assertEquals("start end", StringUtils.flat("start"+noBreakSpace+"end"));
		assertEquals("start end", StringUtils.flat(noBreakSpace+"start"+noBreakSpace+"end"+noBreakSpace));
		assertEquals("a ce", StringUtils.flat("á"+noBreakSpace+"çé"));
		//assertEquals("my-name-is", StringUtils.flat("my-name-is"));
	}
	
	@Test
	public void testNormalizePath()
	{
        assertEquals("/path",		StringUtils.normalizePath("/path"));
        assertEquals("/path",		StringUtils.normalizePath("/path/"));
        assertEquals("/path",		StringUtils.normalizePath("/path//"));
        assertEquals("/path/a", 	StringUtils.normalizePath("/path//a"));
        assertEquals("/path/a", 	StringUtils.normalizePath("/path//a/"));
        assertEquals("/path",		StringUtils.normalizePath("/path///"));
        assertEquals("/path/a", 	StringUtils.normalizePath("/path/a//"));
        assertEquals("/path/a", 	StringUtils.normalizePath("/path//a/"));
        assertEquals("/path/a", 	StringUtils.normalizePath("/path///a"));
        assertEquals("/path/a", 	StringUtils.normalizePath("/path///a/"));
        assertEquals("/path/a/b/c", StringUtils.normalizePath("/path///a/b//c/"));
        assertEquals("/path", 		StringUtils.normalizePath("/path/.."));
        assertEquals("/path", 		StringUtils.normalizePath("/path/../"));
        assertEquals("/path", 		StringUtils.normalizePath("/path/../.."));
        assertEquals("/path/a", 	StringUtils.normalizePath("/path/../../a"));
        assertEquals("/path/a/b", 	StringUtils.normalizePath("/path/../../a/b"));
        assertEquals("/path/a/b", 	StringUtils.normalizePath("/path/../../a/..b"));
        assertEquals("/path/a/b", 	StringUtils.normalizePath("/path/../../a/../b"));
        assertEquals("/path/a/b/c", StringUtils.normalizePath("/path/../../a/../../b/c"));
	}

    @Test
    public void testJoin()
    {
        assertEquals("a,b,c,d", StringUtils.joinIgnoringNulls(new String[]{"a","b","c","d"}, ","));
        assertEquals(null, StringUtils.joinIgnoringNulls(new String[]{null,null,null,null}, ","));
        
        assertEquals("b,c,d", StringUtils.joinIgnoringNulls(new String[]{null,"b","c","d"}, ","));
        assertEquals("a,c,d", StringUtils.joinIgnoringNulls(new String[]{"a",null,"c","d"}, ","));
        assertEquals("a,b,d", StringUtils.joinIgnoringNulls(new String[]{"a","b",null,"d"}, ","));
        assertEquals("a,b,c", StringUtils.joinIgnoringNulls(new String[]{"a","b","c",null}, ","));
        
        assertEquals("a,b", StringUtils.joinIgnoringNulls(new String[]{"a","b",null,null}, ","));
        assertEquals("a,c", StringUtils.joinIgnoringNulls(new String[]{"a",null,"c",null}, ","));
        assertEquals("a,d", StringUtils.joinIgnoringNulls(new String[]{"a",null,null,"d"}, ","));
        assertEquals("b,c", StringUtils.joinIgnoringNulls(new String[]{null,"b","c",null}, ","));
        assertEquals("b,d", StringUtils.joinIgnoringNulls(new String[]{null,"b",null,"d"}, ","));
        assertEquals("c,d", StringUtils.joinIgnoringNulls(new String[]{null,null,"c","d"}, ","));
        
        assertEquals("a", StringUtils.joinIgnoringNulls(new String[]{"a",null,null,null}, ","));
        assertEquals("b", StringUtils.joinIgnoringNulls(new String[]{null,"b",null,null}, ","));
        assertEquals("c", StringUtils.joinIgnoringNulls(new String[]{null,null,"c",null}, ","));
        assertEquals("d", StringUtils.joinIgnoringNulls(new String[]{null,null,null,"d"}, ","));
    }

    @Test
    public void testFindWithCollator()
        throws Exception
    {
        Locale locale = new Locale("pt", "BR");
        Collator collator = Collator.getInstance(locale);
        collator.setStrength(Collator.PRIMARY);

        String[] anyOf = new String[] {
                "aa",
                "bb cc",
                "éé",
                "my house"
        };
        
        assertEquals(false, StringUtils.findAnyOfWithCollator(anyOf, toString("This is a sample"), collator));
        
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("aa"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("aa xx"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("xx aa"), collator));
        
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("bb cc"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("x bb cc"), collator));
        assertEquals(false, StringUtils.findAnyOfWithCollator(anyOf, toString("bb x cc"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("bb cc x"), collator));

        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("éé"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("ee"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("eé"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("êe"), collator));

        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("x éé"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("x ee"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("x éé x"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("x ee x"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("éé x"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("ee x"), collator));

        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my my house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my my my house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my my my house house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my my house house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my house house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my house my house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my car my house"), collator));
        assertEquals(false, StringUtils.findAnyOfWithCollator(anyOf, toString("house my"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("house my house"), collator));
        assertEquals(false, StringUtils.findAnyOfWithCollator(anyOf, toString("bb my cc house"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("bb my house cc"), collator));
        assertEquals(true, StringUtils.findAnyOfWithCollator(anyOf, toString("my bb cc"), collator));
    }
    
    private String[] toString(String s)
    {
        return s.split(StringUtils.SPACE);
    }
    
    @Test
    public void testToPercent()
    	throws Exception
    {
    	assertEquals("50.0%", StringUtils.toPercent(50.0, 100.0));
    	assertEquals("100.0%", StringUtils.toPercent(1.0, 1.0));
    	assertEquals("2.3%", StringUtils.toPercent(23.0, 1000.0));
    	assertEquals("2.3%", StringUtils.toPercent(235.0, 10000.0));
    	assertEquals("0.2%", StringUtils.toPercent(235.0, 100000.0));
    	
    	
    	assertEquals("23.0%", StringUtils.toPercent(.23));
    	assertEquals("2.3%", StringUtils.toPercent(.023));
    	assertEquals("0.2%", StringUtils.toPercent(.0023));
    	assertEquals("50.0%", StringUtils.toPercent(.5));
    	assertEquals("100.0%", StringUtils.toPercent(1));
    }

    @Test
    public void testUnitFormat()
    {
    	assertEquals("100.0 ", StringUtils.unitFormat(100));
    	assertEquals("1.0 K", StringUtils.unitFormat(1000));
    	assertEquals("2.0 K", StringUtils.unitFormat(2000));
    	assertEquals("1.0 M", StringUtils.unitFormat(1000_000));
    	assertEquals("1.5 M", StringUtils.unitFormat(1500_000));
    	assertEquals("1.0 G", StringUtils.unitFormat(1000_000_000));
    	assertEquals("1.0 T", StringUtils.unitFormat(1000_000_000_000l));
    	assertEquals("1.0 P", StringUtils.unitFormat(1000_000_000_000_000l));
    	assertEquals("1.0 E", StringUtils.unitFormat(1000_000_000_000_000_000l));
    	assertEquals("9.2 E", StringUtils.unitFormat(Long.MAX_VALUE));
    }
}
