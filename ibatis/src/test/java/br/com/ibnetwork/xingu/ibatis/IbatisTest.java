package br.com.ibnetwork.xingu.ibatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.ibatis.test.Sample;

public class IbatisTest
    extends XinguTestCase
{

	@Inject
    private Ibatis ibatis;
    
//	public static TestSuite suite()
//    {
//    	TestSuite suite = new TestSuite();
//    	suite.addTest(new IbatisTest("testGetById"));
//    	suite.addTest(new IbatisTest("testGetAll"));
//    	suite.addTest(new IbatisTest("testInsert"));
//    	suite.addTest(new IbatisTest("testUpdate"));
//    	suite.addTest(new IbatisTest("testDelete"));
//    	return suite;
//    }

	@Test
    public void testGetById()
        throws Exception
    {
        Sample sample = (Sample) ibatis.getSqlMap().queryForObject("sample.getById", new Long(1));
        assertEquals(1,sample.getId());
        assertEquals("text 1",sample.getText());
    }
    
	@Test
    public void testGetAll()
        throws Exception
    {
        List all = ibatis.getSqlMap().queryForList("sample.getAll",null);
        assertEquals(2,all.size());
        int i = 1;
        for (Iterator iter = all.iterator(); iter.hasNext();)
        {
            Sample sample = (Sample) iter.next();
            assertEquals(i,sample.getId());
            assertEquals("text "+i,sample.getText());
            i++;
        }
    }

    //TODO: test self generated ids
	@Test
    public void testInsert()
        throws Exception
    {
        Sample sample = new Sample();
        sample.setId(3);
        sample.setText("text 3");
        ibatis.getSqlMap().insert("sample.insert", sample);
        
        Sample stored = (Sample) ibatis.getSqlMap().queryForObject("sample.getById", new Long(3));
        assertNotSame(sample, stored);
        
        assertEquals(3,stored.getId());
        assertEquals("text 3",stored.getText());
    }
    
	@Test
    public void testUpdate()
        throws Exception
    {
        Sample sample = (Sample) ibatis.getSqlMap().queryForObject("sample.getById", new Long(3));
        assertEquals(3,sample.getId());
        assertEquals("text 3",sample.getText());
        
        sample.setText("new text");
        ibatis.getSqlMap().update("sample.update", sample);

        Sample stored = (Sample) ibatis.getSqlMap().queryForObject("sample.getById", new Long(3));
        assertNotSame(sample, stored);
        
        assertEquals(3,stored.getId());
        assertEquals("new text",stored.getText());
    }
    
	@Test
    public void testDelete()
        throws Exception
    {
        List all = ibatis.getSqlMap().queryForList("sample.getAll",null);
        assertEquals(3,all.size());
        Long id = new Long(3);
        Sample sample = (Sample) ibatis.getSqlMap().queryForObject("sample.getById", id);
        assertEquals(3,sample.getId());
        
        ibatis.getSqlMap().delete("sample.delete", id);

        Sample deleted = (Sample) ibatis.getSqlMap().queryForObject("sample.getById", id);
        assertNull(deleted);

        List allAfterDelete = ibatis.getSqlMap().queryForList("sample.getAll",null);
        assertEquals(2,allAfterDelete.size());
    }        
}
