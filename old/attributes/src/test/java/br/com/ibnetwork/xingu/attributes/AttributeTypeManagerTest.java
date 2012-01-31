package br.com.ibnetwork.xingu.attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import br.com.ibnetwork.xingu.attributes.impl.AttributeTypeImpl;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class AttributeTypeManagerTest 
	extends XinguTestCase
{
	AttributeTypeManager attributeTypeManager;
	
	@Before
	public void setUp()
		throws Exception
	{
		attributeTypeManager = (AttributeTypeManager) getContainer().lookup(AttributeTypeManager.class);
	}
	
	@Test
	public void testGetAll()
		throws Exception
	{
		Collection attrs = attributeTypeManager.getAllTypes();
		assertEquals(8,attrs.size());
	}
	
	@Test
	public void testGetByName()
		throws Exception
	{
		AttributeType name = attributeTypeManager.getAttributeTypeByName("name");
		assertEquals(String.class,name.getJavaType());
		AttributeType equal = new AttributeTypeImpl("NamE",String.class);
		assertEquals(equal,name);
		AttributeType notEqual = new AttributeTypeImpl("NamE");
		assertFalse(notEqual.equals(name));
	}
}
