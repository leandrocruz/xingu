package br.com.ibnetwork.xingu.attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.XinguTestCase;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 */
public class AttributeListManagerTest 
	extends XinguTestCase
{
    private AttributeTypeManager attrTypeManager;

    private AttributeListManager alm;
	
    private AttributeType name;

    private AttributeType gender;

	private AttributeType male;

	private AttributeType birth;
	
    private Attribute attrName;

    private Attribute attrGender;
    
	private Attribute attrMale;
    
    protected String componentKey;
    
//    static {
//        COMPONENT_NAME = "attributes";
//    }


    @Before
    public void setUp()
    	throws Exception
    {
    	alm = (AttributeListManager) getContainer().lookup(AttributeListManager.class);
		attrTypeManager = (AttributeTypeManager) getContainer().lookup(AttributeTypeManager.class);
        name = attrTypeManager.getAttributeTypeByName("NAME");
        gender = attrTypeManager.getAttributeTypeByName("GENDER");
		male = attrTypeManager.getAttributeTypeByName("Male");
		birth = attrTypeManager.getAttributeTypeByName("BIRTH");
        attrName = alm.create(name.getTypeName(),"myName");
        attrGender = alm.create(gender.getTypeName(),"f");
		attrMale = alm.create(male.getTypeName(),"false");
    }
    
    @After
    public void tearDown()
        throws Exception
    {
    	alm = null;
    }

//    public static TestSuite suite()
//    {
//    	TestSuite suite = new TestSuite();
//    	suite.addTest(new AttributeListManagerTest("testGetListByIdNoAttributes"));
//    	suite.addTest(new AttributeListManagerTest("testGetListByIdWithAttributes"));
//    	suite.addTest(new AttributeListManagerTest("testCreateFromMap"));
//    	suite.addTest(new AttributeListManagerTest("testStoreNewList"));
//    	suite.addTest(new AttributeListManagerTest("testUpdateList"));
//    	suite.addTest(new AttributeListManagerTest("testGetById"));
//    	suite.addTest(new AttributeListManagerTest("testStoreNewListForGroupName"));
//    	suite.addTest(new AttributeListManagerTest("testRetrieveDate"));
//    	suite.addTest(new AttributeListManagerTest("testStoreDate"));
//    	suite.addTest(new AttributeListManagerTest("testStoreUnparsableDate"));
//    	suite.addTest(new AttributeListManagerTest("testCreateAttribute"));
//    	suite.addTest(new AttributeListManagerTest("testSetAttribute"));
//    	return suite;
//    }
    
    @Test
    public void testGetListByIdNoAttributes()
    	throws Exception
    {
    	AttributeList list = alm.getListById(1);
    	assertEquals(1,list.getId());
    	assertEquals(0,list.getAttributes().size());
        assertEquals("default.default",list.getNameSpace());
    }

    @Test
	public void testGetListByIdWithAttributes()
		throws Exception
	{
		AttributeList list = alm.getListById(2);
		assertEquals(2,list.getId());
		assertEquals(4,list.getAttributes().size());
		
		//types
		assertTrue(list.contains(name));
		assertTrue(list.contains(gender));
		Attribute attr = list.get(gender);
		assertEquals(Character.class,attr.getType().getJavaType());

		//attrs
		assertTrue(list.contains(attrName));
		assertTrue(list.contains(attrGender)); 		 
		Character genderValue =  (Character) attr.getValue();
		assertEquals('f',genderValue.charValue());

	}
	
    @Test
	public void testCreateFromMap()
		throws Exception
	{
		Map map = new HashMap(10);
		map.put("name","myName");
		map.put("gender","f");
		map.put("NEW ATTRIBUTE","value"); // not present
		AttributeList list = alm.create(map);
		assertEquals(-1,list.getId());
		assertEquals(3,list.getAttributes().size());
		
		//types
		assertTrue(list.contains(name));
		assertTrue(list.contains(gender));
		
		//attrs
		assertTrue(list.contains(attrName));
		assertTrue(list.contains(attrGender)); 		 
	}

    @Test
	public void testStoreNewList()
		throws Exception
	{
		Map map = new HashMap(10);
		String name = "�����";
		Date now = new Date();
		map.put("name",name);
		map.put("gender","f");
		map.put("birth",now);
		map.put("NEW ATTRIBUTE","value"); // not present
		AttributeList list = alm.create(map);
		assertTrue(list.getId() < 0);
		long id = alm.store(list);
		AttributeList newList = alm.getListById(id);
		assertEquals(4,newList.getAttributes().size());		
		assertTrue(newList.contains(attrName.getType()));
		assertEquals(name,newList.get("name").getValue());
		assertTrue(newList.contains(attrGender));
		assertTrue(newList.contains(birth));	
		Attribute attr = alm.create(birth.getTypeName(),now.getTime()+"");
		assertTrue(newList.contains(attr));	
		alm.remove(id);
		try
		{
			alm.getListById(id);
			fail("Should throws AttributeException");
		}
		catch(AttributeException e)
		{
			//ignored
		}
	}
	
    @Test
	public void testUpdateList()
		throws Exception
	{
		Map map = new HashMap(10);
		map.put("name","myName");
		map.put("gender","f");
		map.put("NEW ATTRIBUTE","value"); // not present
		AttributeList list = alm.create(map);
		assertTrue(list.getId() < 0);
		long id = alm.store(list);
		AttributeList newList = alm.getListById(id);
		assertEquals(3,newList.getAttributes().size());		
		assertTrue(newList.contains(attrName));
		assertTrue(newList.contains(attrGender));
		alm.remove(id);
		try
		{
			alm.getListById(id);
			fail("Should throws AttributeException");
		}
		catch(AttributeException e)
		{
			//ignored
		}
		
		map.remove("gender");
		newList = alm.create(map);
		newList.setId(id);
		long newId = alm.store(newList);
		assertEquals(id,newId);
		assertEquals(2,newList.getAttributes().size());		
		assertTrue(newList.contains(attrName));
		assertFalse(newList.contains(attrGender));
		
		alm.remove(newId);
		try
		{
			alm.getListById(newId);
			fail("Should throws AttributeException");
		}
		catch(AttributeException e)
		{
			//ignored
		}
	}
	
    @Test
	public void testGetById()
		throws Exception
	{
		try
		{
			alm.getListById(1000);
			fail("no list with id 1000 !");
		}
		catch(AttributeException e)
		{
			//ignored
		}		
	}
	
    @Test
	public void testStoreNewListForGroupName()
		throws Exception
	{
		Map map = new HashMap(10);
		map.put("name","myName");
		map.put("gender","f");
		AttributeList list = alm.create(map);
		assertTrue(list.getId() < 0);
		long id = alm.store("group1", list);
		AttributeList newList = alm.getListById("group1", id);
		assertEquals(2,newList.getAttributes().size());
        assertEquals("group1.repo1",newList.getNameSpace());
		assertTrue(newList.contains(attrName));
		assertTrue(newList.contains(attrGender));
		
		alm.remove("group1", id);
		try
		{
			alm.getListById("group1", id);
			fail("Should throws AttributeException");
		}
		catch(AttributeException e)
		{
			//ignored
		}
		
		//mesma coisa, mas em outro grupo
		Map map1 = new HashMap(10);
		map1.put("name","myName");
		map1.put("gender",new Character('f'));
		AttributeList list1 = alm.create(map1);
		assertTrue(list1.getId() < 0);
		long id1 = alm.store("repo1", list1);
		AttributeList newList1 = alm.getListById("repo1", id1);
		assertEquals(2,newList1.getAttributes().size());
        assertEquals("default.repo1",newList1.getNameSpace());
		assertTrue(newList1.contains(attrName));
		assertTrue(newList1.contains(attrGender));
		
		alm.remove("repo1", id1);
		try
		{
			alm.getListById("repo1", id1);
			fail("Should throws AttributeException");
		}
		catch(AttributeException e)
		{
			//ignored
		}
	}
	
    @Ignore
	public void testNewRepo()
		throws Exception
	{
		Map map = new HashMap(10);
		map.put("name","myName");
		map.put("gender","myGender");
		AttributeList list = alm.create(map);
		alm.store("new-repo",list);
        list = alm.getListById(list.getId());
        assertEquals("default.new-repo",list.getNameSpace());
        assertEquals(2,list.getAttributes().size());
	}

    @Test
	public void testRetrieveDate()
		throws Exception
	{
		AttributeList list = alm.getListById(3);
		Attribute attr = list.get("date");
		assertEquals(Date.class,attr.getType().getJavaType());
		String storedValue = attr.getStoredValue();
		assertEquals("10000000",storedValue);
		Object value = attr.getValue();
		int time = Integer.parseInt(storedValue);
		assertEquals(new Date(time),value);
	}
	
    @Test
	public void testStoreDate()
		throws Exception
	{
		Map map = new HashMap();
		Date now = new Date();
		long time = now.getTime();
		map.put("date",now);
		AttributeList list = alm.create(map);
		assertTrue(list.getId() < 0);
		long id = alm.store(list);
		AttributeList newList = alm.getListById(id);
		Attribute attr = newList.get("date");
		assertEquals(now,attr.getValue());
		assertEquals(String.valueOf(time),attr.getStoredValue());
	}

    @Test
	public void testStoreUnparsableDate()
		throws Exception
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("date",UnparsableDate.INSTANCE);
		AttributeList list = alm.create(map);
		Attribute attr = list.get("date");
        assertNull(attr.getStoredValue());
        assertNull(attr.getValue());
		
        AttributeType type = attrTypeManager.getAttributeTypeByName("date");
		assertTrue(list.contains(type));
		long id = alm.store(list);
		list = alm.getListById(id);
        
		assertTrue(list.contains(type));
        attr = list.get("date");
        assertNull(attr.getStoredValue());
        assertNull(attr.getValue());
	}
	
//	public void testQueryList()
//		throws Exception
//	{
//		AttributeList[] lists = alm.queryList(attrName);
//		assertEquals(1,lists.length);
//		AttributeList list = lists[0];
//		assertEquals(2,list.getId());
//	}
//
//	public void testQueryListSameTypeDifferentValue()
//		throws Exception
//	{
//		Attribute attr = alm.create(name.getTypeName(),"ghost");
//		AttributeList[] lists = alm.queryList(attr);
//		assertEquals(0,lists.length);
//	}

    @Test
	public void testCreateAttribute()
		throws Exception
	{
		Attribute attr = alm.create("ID", new Long(10));
		assertEquals("10",attr.getStoredValue());
		assertEquals("ID",attr.getType().getTypeName());
		assertEquals(Long.class,attr.getType().getJavaType());
		
		attr = alm.create("MALE", Boolean.TRUE);
		assertEquals(Boolean.TRUE,attr.getValue());
		assertEquals("MALE",attr.getType().getTypeName());
		assertEquals(Boolean.class,attr.getType().getJavaType());
		
		attr = alm.create("NEW_ATTR", "value");
		assertEquals("value",attr.getValue());
		assertEquals("NEW_ATTR",attr.getType().getTypeName());
		assertEquals(String.class,attr.getType().getJavaType());
	}
	
    @Test
	public void testSetAttribute()
		throws Exception
	{
		AttributeList list = alm.create(new HashMap());
		assertEquals(0,list.getAttributes().size());
		Attribute attr = list.get("SAMPLE");
		assertNull(attr);
		list.put("SAMPLE", "value");
		attr = list.get("SAMPLE");
		assertEquals(1,list.getAttributes().size());
		assertEquals("SAMPLE",attr.getType().getTypeName());
		assertEquals("value",attr.getValue());
	}
}
