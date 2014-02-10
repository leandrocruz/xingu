package xingu.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;


public class VelocityTemplateEngineTest
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
        String expected = "Name: Leandro R�drigo Saad Cruz" + SystemUtils.LINE_SEPARATOR +  "Email: leandro@ibnetwork.com.br" + SystemUtils.LINE_SEPARATOR +  "$unknown";
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
