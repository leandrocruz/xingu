package br.com.ibnetwork.xingu.flow.test.simple;

import br.com.ibnetwork.xingu.flow.Workflow;

public class MyWorkflow implements Workflow<MyBusinessFacade> 
{

	public final static transient String GET_CURRENT_DATE 	= "CurrentDate";
	public final static transient String IS_COOL 			= "IsCool";
	public final static transient String GET_NAME 			= "GetName";
	
	//Start
	public String getFirstStep() 
	{
		return GET_NAME;
	}
	
	//WorkFlow
	public String getNextStep(String currentState, MyBusinessFacade facade) 
	{
		if(currentState.equals(GET_NAME))
		{   
			return GET_CURRENT_DATE;
	    }
		else if(currentState.equals(GET_CURRENT_DATE))
		{ 
			return IS_COOL;
		}
		else if(currentState.equals(IS_COOL))
		{ 
			if(facade.isCool())
			{
				return COMMIT;
			}
			else
			{
				return ROLLBACK;
			}
		}
		return END;
	}
}
