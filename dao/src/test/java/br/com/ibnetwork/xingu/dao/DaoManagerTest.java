package br.com.ibnetwork.xingu.dao;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.dao.impl.ibatis.GenericDao;
import br.com.ibnetwork.xingu.dao.test.NoDao;
import br.com.ibnetwork.xingu.dao.test.NoDao2;
import br.com.ibnetwork.xingu.dao.test.Sample;
import br.com.ibnetwork.xingu.dao.test.dao.SampleDao;


public class DaoManagerTest
    extends XinguTestCase
{
	@Inject
	private DaoManager daoManager;
	
	@Test
	public void testDefaultDao()
		throws Exception
	{
		Dao dao = daoManager.getDao(Sample.class);
		assertTrue(dao instanceof SampleDao);
	}

	@Test
	public void testGenericDao()
		throws Exception
	{
		Dao dao = daoManager.getDao(NoDao.class);
		assert(dao instanceof GenericDao);
		GenericDao generic = (GenericDao) dao;
		assertEquals("noDao",generic.getNameSpace());
	}
	
	@Test
	public void testDaoCache()
		throws Exception
	{
		Dao dao1 = daoManager.getDao(Sample.class);
		Dao dao2 = daoManager.getDao(Sample.class);
		assertSame(dao1, dao2);

		dao1 = daoManager.getDao(NoDao.class);
		dao2 = daoManager.getDao(NoDao.class);
		assertSame(dao1, dao2);
		
		Dao dao3 = daoManager.getDao(NoDao2.class);
		Dao dao4 = daoManager.getDao(NoDao2.class);
		assertSame(dao3, dao4);
		
		assertNotSame(dao1,dao3);
	}

}
