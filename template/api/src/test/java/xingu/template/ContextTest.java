package xingu.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.template.Context;
import xingu.template.TemplateEngine;

public class ContextTest
	extends XinguTestCase
{
	@Inject
    private TemplateEngine engine;

	@Test
    public void testPut()
    {
        Context ctx = engine.createContext();
        assertEquals(0,ctx.getSize());
        assertFalse(ctx.contains("x"));
        ctx.put("x","y");
        assertTrue(ctx.contains("x"));
        assertEquals("y",ctx.get("x"));
        assertEquals(1,ctx.getSize());
        ctx.put("x","z");
        assertEquals(1,ctx.getSize());
        assertEquals("z",ctx.get("x"));
        ctx.clear();
        assertEquals(0,ctx.getSize());
        assertFalse(ctx.contains("x"));
        assertEquals(null,ctx.get("x"));
    }
    
	@Test
    public void testDelete()
    {
        Context ctx = engine.createContext();
        assertEquals(0,ctx.getSize());
        assertFalse(ctx.contains("x"));
        ctx.put("x","y");
        assertEquals(1,ctx.getSize());
        assertTrue(ctx.contains("x"));
        ctx.delete("x");
        assertFalse(ctx.contains("x"));
        
    }
}
