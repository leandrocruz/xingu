package br.com.ibnetwork.xingu.attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.XinguTestCase;

public class AttributeListTest 
	extends XinguTestCase
{
	private AttributeListManager listManager;

    private AttributeTypeManager attributeTypeManager;
    
    protected String componentKey;
    
    @Before
    public void setUp() 
		throws Exception
	{
		attributeTypeManager = (AttributeTypeManager) getContainer().lookup(AttributeTypeManager.class);
		listManager = (AttributeListManager) getContainer().lookup(AttributeListManager.class);
	}
	
    @After
	public void tearDown()
	    throws Exception
    {
		attributeTypeManager = null;
		listManager = null;
	}
    
    private Attribute createAttribute(AttributeType type, String storedValue) 
        throws Exception
    {
        return createAttribute(type.getTypeName(), storedValue);
    }

    private Attribute createAttribute(String type, String storedValue) 
        throws Exception
    {
        return listManager.create(type, storedValue);
    }

    private AttributeList createList(int id)
    {
        return listManager.create(id, new ArrayList<Attribute>());
    }

    @Test
	public void testAddNull()
	{
		AttributeList attrList = createList(1);
		try
		{
			attrList.put(null);
			fail("Shoud throw exception");
		}
		catch(IllegalArgumentException e)
		{
			// do nothing
		}
	}

    @Test
	public void testReplace()
		throws Exception
	{
		Attribute attrName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"myName");
		Attribute attrOtherName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"otherName");

		AttributeList list = createList(1);
		assertEquals(0,list.size());
		assertFalse(list.contains(attrName));
		assertFalse(list.contains(attrName.getType()));

		//add
		list.put(attrName);
		assertEquals(1,list.size());
		assertTrue(list.contains(attrName));
		assertTrue(list.contains(attrName.getType()));
		assertFalse(list.contains(attrOtherName));
        Attribute attr = list.get("NAME");
        assertEquals("myName",attr.getValue());
		
		//replace
		list.put(attrOtherName);
		assertEquals(1,list.size());
		assertFalse(list.contains(attrName));
		assertTrue(list.contains(attrName.getType()));
		assertTrue(list.contains(attrOtherName));
		attr = list.get("NAME");
        assertEquals("otherName",attr.getValue());
        
        //replace again
        list.put("NAME", "replaced");
        assertEquals(1,list.size());
        assertFalse(list.contains(attrName));
        assertTrue(list.contains(attrName.getType()));
        attr = list.get("NAME");
        assertEquals("replaced",attr.getValue());
	}

    @Test
	public void testContainsAttributeType()
		throws Exception
	{
		AttributeList attrList = createList(1);
		Attribute attrName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"myName");
		Attribute attrGender = createAttribute(attributeTypeManager.getAttributeTypeByName("GENDER"),"myGender");
		attrList.put(attrName);
		assertTrue(attrList.contains(attrName.getType()));
		assertFalse(attrList.contains(attrGender.getType()));
	}
	
    @Test
	public void testAttributeValue()
		throws Exception
	{
		AttributeList attrList = createList(1);
		Attribute attrName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"myName");
		attrList.put(attrName);
		List attributes = attrList.getAttributes();
        Attribute name = (Attribute)attributes.get(0);
        assertEquals("myName",name.getValue());
	}

    @Test
	public void testGetByType()
		throws Exception
	{
		AttributeList attrList = createList(1);
		Attribute attrName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"myName");
		attrList.put(attrName);
		Attribute attr = attrList.get(attributeTypeManager.getAttributeTypeByName("NAME"));
		assertEquals(attributeTypeManager.getAttributeTypeByName("NAME"),attr.getType());
		assertEquals("myName",attr.getValue());
		assertNull(attrList.get(attributeTypeManager.getAttributeTypeByName("GENDER")));
	}
	
    @Test
	public void testContainsAttribute()
		throws Exception
	{
		AttributeList attrList = createList(1);
		Attribute attrName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"any value. doesn't matter");
		Attribute male = createAttribute(attributeTypeManager.getAttributeTypeByName("GENDER"),"m");
		Attribute female = createAttribute(attributeTypeManager.getAttributeTypeByName("GENDER"),"f");
		attrList.put(male);
		assertTrue(attrList.contains(male));
		assertFalse(attrList.contains(female));
		assertFalse(attrList.contains(attrName));
	}	

    @Test
	public void testSuperSet()
		throws Exception
	{
		//base
		AttributeList baseAttrList = createList(1);
		Attribute attrName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"myName");
		Attribute attrGender = createAttribute(attributeTypeManager.getAttributeTypeByName("GENDER"),"myGender");
		baseAttrList.put(attrGender);
		baseAttrList.put(attrName);
		
		//base x base
		assertTrue(baseAttrList.isSuperSet(baseAttrList));

		//base x null
		assertFalse(baseAttrList.isSuperSet(null));
		
		//other1 x base
		AttributeList otherAttrList1 = createList(1);
		assertFalse(otherAttrList1.isSuperSet(baseAttrList));
	

		//other2 x base
		AttributeList otherAttrList2 = createList(2);
		Attribute attrName2 = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"myName2");
		//Attribute attrGender2 = createAttribute(attributeTypeManager.getAttributeTypeByName("GENDER"),"myGender2");
		otherAttrList2.put(attrGender);
		otherAttrList2.put(attrName);
		
		assertTrue(baseAttrList.isSuperSet(otherAttrList2));
		assertTrue(otherAttrList2.isSuperSet(baseAttrList));

		//other3 x base
		AttributeList otherAttrList3 = createList(3);
		otherAttrList3.put(attrName2);
		
		assertTrue(baseAttrList.isSuperSet(otherAttrList3));
		assertFalse(otherAttrList3.isSuperSet(baseAttrList));
		
		
		Attribute attrAddr = createAttribute(attributeTypeManager.getAttributeTypeByName("ADDRESS"),"myAddress");
		otherAttrList3.put(attrAddr);
		
		assertFalse(baseAttrList.isSuperSet(otherAttrList3));
		
		baseAttrList.put(attrAddr);
		
		assertTrue(baseAttrList.isSuperSet(otherAttrList3));
		
	}
	
    @Test
	public void testToMap()
		throws Exception
	{
		AttributeList attrList = createList(1);
		Attribute attrName = createAttribute(attributeTypeManager.getAttributeTypeByName("NAME"),"myName");
		Attribute attrGender = createAttribute(attributeTypeManager.getAttributeTypeByName("GENDER"),"m");
		attrList.put(attrName);
		attrList.put(attrGender);
		Map map = attrList.toMap();
		assertEquals("myName",map.get("NAME"));
		assertEquals(new Character('m'),map.get("GENDER"));
	}

    @Test
	public void testAddAttribute()
		throws Exception
	{
		AttributeList list = listManager.getListById(1);
		int size = list.getAttributes().size();
		Attribute attr = createAttribute("TEST_ADD" ,"value");
        assertFalse(list.contains(attr));
        
        //add attr to list
        list.put(attr);
		listManager.store(list);
		
        assertEquals(size + 1,list.getAttributes().size());
		assertTrue(list.contains(attr));
	}
	
    @Test
	public void testRemoveAttribute()
		throws Exception
	{
		AttributeList list = listManager.getListById(1);
        int size = list.getAttributes().size();
        
		Attribute attr = createAttribute("TEST_REMOVE" ,"value");
		assertFalse(list.contains(attr));
        list.put(attr);
        listManager.store(list);
        assertEquals(size + 1,list.getAttributes().size());
        assertTrue(list.contains(attr));

		list.delete(attr);
		listManager.store(list);
		assertFalse(list.contains(attr));
        assertEquals(size,list.getAttributes().size());
	}

    @Test
	public void testSetAttribute()
		throws Exception
	{
		AttributeList list = listManager.getListById(1);
		Attribute attr = list.get("TEST_SET");
		assertFalse(list.contains(attr));
		AttributeType type = attributeTypeManager.getAttributeTypeByName("TEST_SET");
		Attribute newAttr = createAttribute(type ,"value");
		list.put(newAttr);
		listManager.store(list);
		assertTrue(list.contains(newAttr));
		assertEquals(newAttr, list.get(type));
	}

    @Test
	public void testCreateWithId()
		throws Exception
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ID",new Long(1000));
		AttributeList list = listManager.create(map);
		assertEquals(1000,list.getId());
	}

    @Test
	public void testJavaType()
		throws Exception
	{
		AttributeType name = attributeTypeManager.getAttributeTypeByName("NAME");
		assertEquals(name.getJavaType(),String.class);
		AttributeType birth = attributeTypeManager.getAttributeTypeByName("BIRTH");
		assertEquals(birth.getJavaType(),Date.class);
	}
    
    @Ignore
    public void testNullAttribute()
        throws Exception
    {
        AttributeList list = listManager.create(new HashMap());
        assertEquals(0,list.size());
        Attribute ghost = list.get("GHOST");
        assertNull(ghost.getValue());
        assertNull(ghost.getStoredValue());
    }
}