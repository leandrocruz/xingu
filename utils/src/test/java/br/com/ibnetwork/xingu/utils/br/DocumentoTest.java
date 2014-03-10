package br.com.ibnetwork.xingu.utils.br;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.utils.br.Documento;

public class DocumentoTest
{
	@Test
	public void testCpf()
		throws Exception
	{
		Documento doc = new Documento("123.123.123-87");
		assertEquals("12312312387", doc.getValue());
		assertEquals("123.123.123-87", doc.getValueFormatted());
		assertTrue(doc.isCpf());
		assertTrue(doc.isValid());
		
		assertFalse(doc.isCnpj());
	}

	@Test
	@Ignore
	public void testCnpj()
		throws Exception
	{
		Documento doc = new Documento("10.565.769/0001-00");
		assertEquals("10565769000100", doc.getValue());
		assertEquals("10.565.769/0001-00", doc.getValueFormatted());
		assertTrue(doc.isCnpj());
		assertTrue(doc.isValid());
		
		assertFalse(doc.isCpf());
	}

}
