package br.com.ibnetwork.xingu.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.com.ibnetwork.xingu.attributes.Attribute;
import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.attributes.AttributeListManager;
import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.factory.Factory;

public class UserManagerTest
    extends XinguTestCase
{
    
	@Dependency
    private Factory factory;
    
	@Dependency
    private UserManager userManager;
    
	@Dependency
	private AttributeListManager listManager;
    
    private static long userId;

    @Test
    public void testGetAll()
        throws Exception
    {
        List<User> all = userManager.getAll();
        assertEquals(2,all.size());
        for (User user : all)
        {
            assertTrue(user.isActive());
        }
    }
    
    @Test
    public void testGetById()
        throws UserManagerException
    {
        User user = userManager.getById(1);
        assertEquals(1,user.getId());
        assertEquals("adm",user.getUsername());
        assertFalse(user.hasAttributeList());
        assertEquals(-1,user.getAttributeListId());
        assertTrue(user.isConfirmed());
        assertEquals("code",user.getConfirmationCode());
    }

    @Test
    public void testGetByUsername()
        throws UserManagerException
    {
        User user = userManager.getByUsername("adm");
        assertEquals("adm",user.getUsername());
        assertEquals(1,user.getId());
    }
    
    @Test
    public void testAddUser()
        throws UserManagerException
    {
        User user = (User) factory.create(User.class);
        user.setUsername("joao");
        assertEquals(0,user.getId());
        userManager.store(user);
        userId = user.getId();
        assertTrue(userId > 0);

        User justAdded = userManager.getById(userId);
        assertEquals(user.getUsername(),justAdded.getUsername());
        assertEquals(userId,justAdded.getId());
    }

    @Test
    public void testAddRepeatedUser()
        throws Exception
    {
        try
        {
            User user = (User) factory.create(User.class);
            user.setUsername("adm");
            userManager.store(user);
            fail("Should raise UserManagerException");
        }
        catch(UserManagerException e)
        {
            assertEquals("adm",e.getUsername());
        }
    }

    @Test
    public void testUpdateUser()
        throws UserManagerException
    {
		User user = userManager.getById(userId);
		assertEquals("joao",user.getUsername());
        user.setUsername("joao regis da cruz neto");
        userManager.store(user);
        user = userManager.getById(userId);
        assertEquals("joao regis da cruz neto",user.getUsername());
    }

    @Test
    public void testRemoveUser()
        throws UserManagerException
    {
        User user = userManager.getById(userId);
        userManager.delete(user);
        User removed = userManager.getById(userId);
        assertNull(removed);
    }

    @Test
	public void testAddAttributeList()
		throws Exception
	{
		User user = (User) factory.create(User.class);
        user.setUsername("user-with-attributes");
		Map<String,String> map = new HashMap<String,String>();
		map.put("EMAIL","someone@somewhere.com");
		AttributeList attrList = listManager.create(map);
        assertEquals(0,user.getId());
        assertEquals(-1,user.getAttributeListId());
        
        user.setAttributeList(attrList);
		userManager.store(user);
        assertTrue(user.getId() > 0);
        
        AttributeList list = user.getAttributeList();
        assertTrue(list.getId() > 0);
        assertEquals(1,list.getAttributes().size());
        assertEquals("someone@somewhere.com",list.get("EMAIL").getValue());
    }

    @Test
    public void testUpdateInexistentUser()
        throws Exception
    {
        try
        {
            User user = (User) factory.create(User.class);
            user.setUsername("neto");
            user.setId(1000);
            userManager.store(user);
        }
        catch(UserManagerException e)
        {
            assertEquals(1000,e.getUserId());
        }
    }

    @Test
    public void testRemoveInexistentUser()
        throws Exception
    {
        User user = (User) factory.create(User.class);
        user.setId(1000);

        try
        {
            userManager.delete(user);
        }
        catch(UserManagerException e)
        {
            assertEquals(1000,e.getUserId());
        }
    }
    
    @Test
    public void testAttributeList()
    	throws Exception
    {
    	User user = userManager.getById(2);
    	assertTrue(user.hasAttributeList());
    	assertEquals(1,user.getAttributeListId());
    	AttributeList attrList = user.getAttributeList();
    	assertNotNull(attrList);
		assertEquals(2,attrList.getAttributes().size());
    	Attribute attr = attrList.get("NAME");
    	assertEquals("someone",attr.getValue());
    }
    
    @Test
    public void testUserEquals()
    	throws Exception
    {
		User user1 = userManager.getById(2);
		User user2 = userManager.getById(2);
		
		assertEquals(user1, user2);
    }
}
