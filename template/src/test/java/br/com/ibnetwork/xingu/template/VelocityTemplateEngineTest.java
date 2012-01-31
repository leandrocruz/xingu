package br.com.ibnetwork.xingu.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;


/**
 * @author leandro
 */
public class VelocityTemplateEngineTest
    extends XinguTestCase
{
	@Inject("velocity")
    private TemplateEngine engine;
    
	@Test
    public void testTemplateExists()
    	throws Exception
    {
        assertTrue(engine.templateExists("test/Exists"));
        assertTrue(engine.templateExists("test.Exists"));
        assertTrue(engine.templateExists("test.Exists.vm"));
        assertFalse(engine.templateExists("test.Exists.ftl"));
    }
    
	@Test
    public void testRenderTemplate()
    	throws Exception
    {
        StringWriter sw = new StringWriter();
        Context ctx = engine.createContext();
        ctx.put("myName","Leandro R�drigo Saad Cruz");
        ctx.put("email","leandro@ibnetwork.com.br");
        engine.merge("test/Sample",ctx,sw);
        String expected = "Name: Leandro R�drigo Saad Cruz\nEmail: leandro@ibnetwork.com.br\n$unknown";
        assertEquals(expected,sw.toString());
    }
    
	@Test
    public void testExtension()
		throws Exception
	{
        assertEquals(".vm",engine.getTemplateExtension());
	}

	@Test
    public void testEncoding()
    	throws Exception
    {
        assertEquals("ISO-8859-1",engine.getEncoding());
    }
}
