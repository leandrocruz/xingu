package br.com.ibnetwork.xingu.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;


/**
 * @author leandro
 */
public class TemplateEngineTest
    extends XinguTestCase
{
	@Inject
    private TemplateEngine engine;
    
	@Test
    public void testTemplateExists()
    	throws Exception
    {
        assertTrue(engine.templateExists("test/Exists"));
        assertTrue(engine.templateExists("test.Exists"));
        assertTrue(engine.templateExists("test.Exists.ftl")); //.freemarker
        assertTrue(engine.templateExists("test.OnlyOnFreemarker")); //.freemarker
        assertTrue(engine.templateExists("test.OnlyOnFreemarker.ftl")); //.freemarker
        assertTrue(engine.templateExists("test.Exists.vm")); //.velocity
        assertTrue(engine.templateExists("test.OnlyOnVelocity")); //.velocity
        assertTrue(engine.templateExists("test.OnlyOnVelocity.vm")); //.velocity
        assertTrue(engine.templateExists("test.Exists.gtl")); //.groovy
        assertTrue(engine.templateExists("test.OnlyOnGroovy")); //.groovy
        assertTrue(engine.templateExists("test.OnlyOnGroovy.gtl")); //.groovy
    }
    
	@Test
    public void testRenderTemplate()
    	throws Exception
    {
        Context ctx = engine.createContext();
        ctx.put("myName","Leandro Rodrigo Saad Cruz");
        ctx.put("email","leandro@ibnetwork.com.br");
        String expected = "Name: Leandro Rodrigo Saad Cruz\nEmail: leandro@ibnetwork.com.br\n$unknown";
        assertRenderingResults("test/Sample",ctx,expected);
    }

	@Test
    public void testRenderingOrder()
        throws Exception
    {
        Context ctx = engine.createContext();
        assertRenderingResults("test.Exists",ctx,"Exists - Velocity");
        assertRenderingResults("test.Exists.vm",ctx,"Exists - Velocity");
        assertRenderingResults("test.Exists.ftl",ctx,"Exists - Freemarker");
        assertRenderingResults("test.Exists.gtl",ctx,"Exists - Groovy");
        assertRenderingResults("test.OnlyOnVelocity",ctx,"Velocity");
        assertRenderingResults("test.OnlyOnVelocity.vm",ctx,"Velocity");
        assertRenderingResults("test.OnlyOnFreemarker",ctx,"Freemarker");
        assertRenderingResults("test.OnlyOnFreemarker.ftl",ctx,"Freemarker");
        assertRenderingResults("test.OnlyOnGroovy",ctx,"Groovy");
        assertRenderingResults("test.OnlyOnGroovy.gtl",ctx,"Groovy");
    }
    
    private void assertRenderingResults(String templateName, Context ctx, String expected)
    {
        StringWriter sw = new StringWriter();
        engine.merge(templateName,ctx,sw);
        String result = sw.toString();
        assertEquals(expected,result);
    }

    @Test
    public void testExtension()
    	throws Exception
    {
        assertEquals(".txt",engine.getTemplateExtension());
    }
    
    @Test
    public void testTransformTemplateName() 
        throws Exception 
    {
        assertEquals("a.b.c",engine.toTemplateName("/a/b/c"));
        assertEquals("a.b.c",engine.toTemplateName("/a/b/c.txt"));
        assertEquals("a.b.c",engine.toTemplateName("a/b/c"));
        assertEquals("a.b.c",engine.toTemplateName("a/b/c.txt"));
    }

    @Test
    public void testReverseTemplateName() 
        throws Exception 
    {
        assertEquals("a/b/c.txt",engine.toFileName("a.b.c"));
    }
}
