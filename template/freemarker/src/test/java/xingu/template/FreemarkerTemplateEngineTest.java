package xingu.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import xingu.template.impl.freemarker.FreemarkerTemplateEngine;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class FreemarkerTemplateEngineTest
    extends XinguTestCase
{
	@Inject
	private TemplateEngine engine;

	@Override
	protected String getContainerFile()
	{
		return "pulga.xml";
	}

	@Test
    public void testTemplateExists()
    	throws Exception
    {
        assertTrue(engine.templateExists("test/Exists"));
        assertTrue(engine.templateExists("test.Exists"));
        assertTrue(engine.templateExists("test.Exists.ftl"));
        assertFalse(engine.templateExists("test.Exists.vm"));
    }
    
	@Test
    public void testRenderTemplate()
    	throws Exception
    {
        StringWriter sw = new StringWriter();
        Context ctx = engine.createContext();
        ctx.put("myName","Leandro Rodrigo Saad Cruz");
        ctx.put("email","leandro@ibnetwork.com.br");
        engine.merge("test/Sample",ctx,sw);
        String expected = "Name: Leandro Rodrigo Saad Cruz" + SystemUtils.LINE_SEPARATOR +  "Email: leandro@ibnetwork.com.br" + SystemUtils.LINE_SEPARATOR +  "$unknown";
        sw.toString();
        assertEquals(expected,sw.toString());
    }
    
	@Test
    public void testExtension()
    	throws Exception
    {
        assertEquals(".ftl",engine.getTemplateExtension());
    }
    
	@Test
    public void testAutoImport()
    	throws Exception
    {
        StringWriter sw = new StringWriter();
        Context ctx = engine.createContext();
        ctx.put("var","Leandro");
        engine.merge("test/AutoImport",ctx,sw);
        String expected = "sampleMacro 1: Leandro" + SystemUtils.LINE_SEPARATOR +  "sampleMacro 2: Leandro" + SystemUtils.LINE_SEPARATOR;
        sw.toString();
        assertEquals(expected,sw.toString());
    }
    
	@Test
    public void testMethod()
        throws Exception
    {
        StringWriter sw = new StringWriter();
        Context ctx = engine.createContext();
        ctx.put("var","leandro");
        engine.merge("test/Method",ctx,sw);
        String expected = "LEANDRO";
        sw.toString();
        assertEquals(expected,sw.toString());
    }
    
	@Test
    public void testLoadFromClasspath()
    	throws Exception
    {
    	assertTrue(engine.templateExists("templates/onClasspath/OnClassPath"));
    	assertTrue(engine.templateExists("/templates/onClasspath/OnClassPath"));
    	assertTrue(engine.templateExists("templates.onClasspath.OnClassPath"));
    	assertTrue(engine.templateExists(".templates.onClasspath.OnClassPath"));
    }

	@Test
    public void testParseInvalidReferenceExceptionMessage()
    	throws Exception
    {
    	String sample = "Expression xx is undefined on line 2, column 11 in screens/Test.ftl.";
    	Pattern p = FreemarkerTemplateEngine.invalidReference;
    	Matcher m = p.matcher(sample);
    	m.matches(); //need to call before m.group() is called
    	assertEquals("xx",m.group(1));
    	assertEquals("2",m.group(2));
    	assertEquals("11",m.group(3));
    }

	@Test
    public void testGetRawText()
    	throws Exception
    {
    	String text = engine.getRawText("test.Method");
    	assertEquals("${testMethod(var)}",text.trim());

    	text = engine.getRawText("test/Method.ftl");
    	assertEquals("${testMethod(var)}",text.trim());
    }
    
	@Test
    public void testErrorHighlighter()
    	throws Exception
    {
    	String rawText =""
    		+ "first line\n"
    		+"<h1>some text</h1>\n"
    		+"<assign value = 'x'/>\n" 
    		+"line with ${error}\n" 
    		+"<h1>more text</h1>\n"; 

    	String expected ="" 
    		+"2: ...\n"
    		+"3: &lt;assign value = 'x'/&gt;\n" 
    		+"<span class=\"error\">4: line with <b>${error}</b></span>\n" 
    		+"5: &lt;h1&gt;more text&lt;/h1&gt;\n";

    	StringWriter sw = new StringWriter();
    	Context ctx = engine.createContext();
    	ctx.put("rawText",rawText);
    	TemplateEngineException tee = new TemplateEngineException("Error", new Exception());
    	tee.setExpression("error");
    	tee.setLineNumber(4);
    	ctx.put("exception", tee);
    	engine.merge("test/ErrorHighlighter",ctx,sw);
    	sw.toString();
    	assertEquals(expected,sw.toString());
	}

	@Test
    public void testExtractErrorMessage()
    {
    	Matcher m;
    	
    	String invalidRef = "Expression xx is undefined on line 2, column 11 in screens/Test.ftl.";
    	m = FreemarkerTemplateEngine.invalidReference.matcher(invalidRef); 
    	assertTrue(m.matches());
    	assertEquals("xx", m.group(1));
    	assertEquals("2", m.group(2));
    	assertEquals("11", m.group(3));
    	
    	String global = "Error on line 69, column 75 in screens/sample/Ajax.ftl\n" +
    			"Expecting a string, date or number here, Expression sample.ativo! is instead a freemarker.template.TemplateBooleanModel$2";
    	m = FreemarkerTemplateEngine.global.matcher(global); 
    	assertTrue(m.matches());
    	assertEquals("69", m.group(1));
    	assertEquals("75", m.group(2));
    	assertEquals("sample.ativo!", m.group(3));
    }
}
