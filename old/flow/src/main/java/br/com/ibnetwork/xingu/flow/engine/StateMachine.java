package br.com.ibnetwork.xingu.flow.engine;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.xingu.flow.BeforeEnterState;
import br.com.ibnetwork.xingu.flow.BeforeExitState;
import br.com.ibnetwork.xingu.flow.BusinessFacade;
import br.com.ibnetwork.xingu.flow.PersistenceAgent;
import br.com.ibnetwork.xingu.flow.Workflow;

public abstract class StateMachine
{
	private static Log log = LogFactory.getLog(StateMachine.class);

	@SuppressWarnings("unchecked")
	public static void execute(Workflow workflow, BusinessFacade facade, PersistenceAgent pa)
	    throws StateMachineException
	{

		DataManager dm = getDataManager(pa);
		dm.synchronizeData(facade);
		String state = dm.getState();

		if (state == null)
		{
			state = Workflow.BEGIN;
		}

		if (state.equals(Workflow.BEGIN))
		{
			getDataManager(pa).clear();
		}

		log("-----------------------------");
		log("-->" + getHistory(pa).toString());
		log("--> initial state:" + state);

		try
		{
			if(executeBeforeExit(facade, state))
			{
				String newState = getCompleteNextStep(workflow, facade, pa, state);
				if(executeBeforeEnter(facade, newState))
				{
					addStateToHistory(pa, state);
					state = newState;
					getDataManager(pa).setState(state);
					log("--> current state:" + state);
					persist(facade, pa);
					logDataManager(pa);
				}
			}
			return;
		}
		catch (Throwable t)
		{
			log("Error. Current state: "+state,t);
		}

//		addStateToHistory(pa, state);
//		log("--> current state:" + state);
//		persist(facade, pa);
//		logDataManager(pa);
	}

	static boolean executeBeforeExit(BusinessFacade facade, String state)
	    throws Exception
	{
		boolean result = true;
		Method methods[] = facade.getClass().getMethods();
		for (Method method : methods)
		{
			BeforeExitState stateAnn = method.getAnnotation(BeforeExitState.class);
			if (stateAnn != null && state.equals(stateAnn.state()))
			{
				result = callMethod(facade, method) && result;
			}
		}
		return result;
	}

	static boolean executeBeforeEnter(BusinessFacade facade, String newState)
	    throws Exception
	{
		boolean result = true;
		Method methods[] = facade.getClass().getMethods();
		for (Method method : methods)
		{
			BeforeEnterState stateAnn = method.getAnnotation(BeforeEnterState.class);
			if (stateAnn != null && newState.equals(stateAnn.state()))
			{
				result = callMethod(facade, method) && result;
			}
		}
		return result;
	}

	public static void back(BusinessFacade facade, PersistenceAgent session)
	{
		Stack<String> history = getHistory(session);
		DataManager dm = getDataManager(session);
		dm.synchronizeData(facade);
		String state = dm.getState();
		log("-----------------------------");
		log("-->" + history.toString());

		if (state.equals(Workflow.END))
		{
			log("--> can't back, end state");
		}
		else if (state.equals(Workflow.BEGIN))
		{
			log("--> can't back, first state");
		}
		else
		{
			if (history.size() > 0)
			{
				state = history.lastElement();
				if (state.equals(Workflow.BEGIN))
				{
					clearData(session);
				}
				getDataManager(session).setState(state);
				history.remove(history.size() - 1);
				log("-->" + history.toString());
			}
			log("--> back to state:" + state);
		}
		persist(facade, session);
		logDataManager(session);
	}

	@SuppressWarnings("unchecked")
	public static void back(BusinessFacade facade, PersistenceAgent session, String state) 
	    throws Exception
	{
		DataManager dm = getDataManager(session);
		dm.synchronizeData(facade);
		Stack<String> history = getHistory(session);
		log("-----------------------------");
		log("-->" + history.toString());

		if (state.equals(Workflow.END))
		{
			log("--> can't back, end state");
		}
		else
		{
			try
			{
				Stack<String> historyTmp = (Stack<String>) history.clone();
				while (!historyTmp.lastElement().equals(state))
				{
					historyTmp.remove(historyTmp.size() - 1);
				}
				setHistory(session, historyTmp);
				history = historyTmp;
				if (state.equals(Workflow.BEGIN))
				{
					clearData(session);
				}
			}
			catch (NoSuchElementException e)
			{
				persist(facade, session);
				throw new StateMachineException("Can't back to state " + state, e);
			}
			history.remove(history.size() - 1);
			log("-->" + history.toString());
			getDataManager(session).setState(state);
			log("--> back to state:" + state);
		}
		persist(facade, session);
		logDataManager(session);
	}

	private static boolean callMethod(BusinessFacade facade, Method method)
	    throws Exception
	{
		Object result = method.invoke(facade, new Object[0]);
		if(result == null)
		{
			return true;
		}
		if(result instanceof Boolean)
		{
			return ((Boolean)result).booleanValue();
		}
		return false;
	}

	private static void addStateToHistory(PersistenceAgent session, String state)
	{
		String last = null;
		Stack<String> history = getHistory(session);

		if (history.size() > 0)
		{
			last = history.lastElement();
		}

		if (last == null || !last.equals(state))
		{
			history.add(state);
		}
		log("-->" + history.toString());
	}

	private static void setHistory(PersistenceAgent session, Stack<String> historyTmp) 
	{
		getDataManager(session).setHistory(historyTmp);
	}

	static DataManager getDataManager(PersistenceAgent session)
	{
		DataManager dataManager = (DataManager) session.load("dataManager");
		if (dataManager == null)
		{
			dataManager = new DataManager();
			session.persist("dataManager", dataManager);
		}
		return dataManager;
	}

	public static Stack<String> getHistory(PersistenceAgent session)
	{
		return getDataManager(session).getHistory();
	}

	private static void persist(BusinessFacade facade, PersistenceAgent session)
	{
		DataManager dm = getDataManager(session);
		dm.setState(getDataManager(session).getState());
		dm.persist(session, facade);
	}

	static String getCompleteNextStep(Workflow<BusinessFacade> workflow, BusinessFacade facade, PersistenceAgent session, String currentState)
	{

		// inicio de fluxo
		if (currentState.equals(Workflow.BEGIN))
		{
			return workflow.getFirstStep();
		}

		// fim de fluxo
		if (currentState.equals(Workflow.COMMIT)
		        || currentState.equals(Workflow.ROLLBACK)
		        || currentState.equals(Workflow.END))
		{
			return Workflow.END;
		}

		return workflow.getNextStep(currentState, facade);
	}

	public static void clearData(PersistenceAgent pa)
	{
		getDataManager(pa).clear(pa);
	}

	public static void log(String msg, Throwable t)
	{
		log.info(msg, t);
	}

	public static void log(String msg)
	{
		log.info(msg);
	}

	private static void logDataManager(PersistenceAgent pa)
	{
		log(getDataManager(pa).toString());
	}

	public static String getState(PersistenceAgent pa)
	{
		return getDataManager(pa).getState();
	}

	public static void setState(PersistenceAgent pa, String newState)
	{
		getDataManager(pa).setState(newState);
	}
}