package br.com.ibnetwork.xingu.utils.test.br;

import junit.framework.TestCase;
import br.com.ibnetwork.xingu.utils.br.Cpf;

public class CpfTest
    extends TestCase
{
	public void testGenerateCpf() 
		throws Exception
	{
		String cpf = Cpf.geraCPF("511.481.301");
		assertEquals("511481301", cpf.substring(0,9));
		assertEquals("36", cpf.substring(9,11));
	}

	public void testExample()
		throws Exception
	{
		String cpf = Cpf.geraCPF("123.123.123");
		assertEquals("12312312387", cpf);
	}
	
	public void testValidate()
		throws Exception
	{
		boolean valid = Cpf.isValid("123.123.123-87");
		assertTrue(valid);

		valid = Cpf.isValid("123.123.121-87");
		assertFalse(valid);

		valid = Cpf.isValid("123.123.123-88");
		assertFalse(valid);

	}
	
	public void testMask()
		throws Exception
	{
		String withMask = "123.123.123-87";
		String noMask   = "12312312387";
		
		assertEquals(noMask, Cpf.clearMask(withMask));
		assertEquals(noMask, Cpf.clearMask(noMask));
		
		assertEquals(withMask, Cpf.addMask(withMask));
		assertEquals(withMask, Cpf.addMask(noMask));
	}
}
