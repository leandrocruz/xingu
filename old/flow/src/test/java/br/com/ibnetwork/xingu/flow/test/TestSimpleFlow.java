package br.com.ibnetwork.xingu.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import br.com.ibnetwork.xingu.flow.PersistenceAgent;
import br.com.ibnetwork.xingu.flow.engine.StateMachine;
import br.com.ibnetwork.xingu.flow.engine.StateMachineException;
import br.com.ibnetwork.xingu.flow.test.simple.MyBusinessFacade;
import br.com.ibnetwork.xingu.flow.test.simple.MyPersistenceAgent;
import br.com.ibnetwork.xingu.flow.test.simple.MyWorkflow;

public class TestSimpleFlow 
{

    @Test
	public void testFirstStep()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);
		assertEquals(new MyWorkflow().getFirstStep(), StateMachine.getState(pa));
	}

    @Test
	public void testInvalidBeforeExit()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName(null);
		StateMachine.execute(new MyWorkflow(), facade, pa);

		assertEquals(MyWorkflow.GET_NAME, StateMachine.getState(pa));
		assertEquals(facade.getName(), null);
	}

    @Test
	public void testValidBeforeExit()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		assertEquals(MyWorkflow.GET_CURRENT_DATE, StateMachine.getState(pa));
		assertEquals(facade.getName(), "Sandro Bihaiko");
	}

    @Test
	public void testBeforeEnter()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());

		Calendar c2 = Calendar.getInstance();
		c2.setTime(facade.getCurrentDate());

		int div = c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);
		assertEquals(div, 1);
	}

    @Test
	public void testDataManager()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.setCurrentDate(new Date());
		StateMachine.execute(new MyWorkflow(), facade, pa);

		assertEquals(facade.getName(), "Sandro Bihaiko");

	}

    @Test
	public void testBackX3()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.setCurrentDate(new Date());
		StateMachine.execute(new MyWorkflow(), facade, pa);

		//Back BEGIN
		StateMachine.back(new MyBusinessFacade(), pa);
		assertEquals(MyWorkflow.GET_CURRENT_DATE, StateMachine.getState(pa));

		StateMachine.back(new MyBusinessFacade(), pa);
		assertEquals(MyWorkflow.GET_NAME, StateMachine.getState(pa));

		StateMachine.back(new MyBusinessFacade(), pa);
		assertEquals(MyWorkflow.BEGIN, StateMachine.getState(pa));

		StateMachine.back(new MyBusinessFacade(), pa);
		assertEquals(MyWorkflow.BEGIN, StateMachine.getState(pa));

	}

    @Test
	public void testBackToBegin()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.setCurrentDate(new Date());
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		StateMachine.back(facade, pa, MyWorkflow.BEGIN);
		assertEquals(MyWorkflow.BEGIN, StateMachine.getState(pa));

		facade = new MyBusinessFacade();
		StateMachine.execute(new MyWorkflow(), facade, pa);

		assertEquals(facade.getName(), null);
	}
    
    
    @Test
	public void testBackToEnd()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		StateMachine.back(facade, pa, MyWorkflow.END);
		assertEquals(MyWorkflow.GET_CURRENT_DATE, StateMachine.getState(pa));
	}

    @Test
	public void testInvalidBack()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		try
		{
			StateMachine.back(facade, pa, MyWorkflow.IS_COOL);
			fail("StateMachineException was expected!");
		}
		catch (StateMachineException ex)
		{
			assertEquals(MyWorkflow.GET_CURRENT_DATE, StateMachine
			        .getState(pa));
		}
	}

    @Test
	public void testCommitFork()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.setCurrentDate(new Date());
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.isCool(true);
		StateMachine.execute(new MyWorkflow(), facade, pa);

		assertEquals(MyWorkflow.COMMIT, StateMachine.getState(pa));
	}

    @Test
	public void testRollbackFork()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.setCurrentDate(new Date());
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.isCool(false);
		StateMachine.execute(new MyWorkflow(), facade, pa);

		assertEquals(MyWorkflow.ROLLBACK, StateMachine.getState(pa));
	}

    @Test
	public void testEndState()
	    throws Exception
	{
		PersistenceAgent pa = new MyPersistenceAgent();
		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);

		MyBusinessFacade facade = new MyBusinessFacade();
		facade.setName("Sandro Bihaiko");
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.setCurrentDate(new Date());
		StateMachine.execute(new MyWorkflow(), facade, pa);

		facade = new MyBusinessFacade();
		facade.isCool(true);
		StateMachine.execute(new MyWorkflow(), facade, pa);

		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);
		assertEquals(MyWorkflow.END, StateMachine.getState(pa));

		StateMachine.execute(new MyWorkflow(), new MyBusinessFacade(), pa);
		assertEquals(MyWorkflow.END, StateMachine.getState(pa));

		StateMachine.back(new MyBusinessFacade(), pa);
		assertEquals(MyWorkflow.END, StateMachine.getState(pa));

		StateMachine.back(new MyBusinessFacade(), pa, MyWorkflow.BEGIN);
		assertEquals(MyWorkflow.BEGIN, StateMachine.getState(pa));
	}
}
