package br.com.ibnetwork.xingu.flow.test.simple;

import java.util.Calendar;
import java.util.Date;

import br.com.ibnetwork.xingu.flow.BeforeEnterState;
import br.com.ibnetwork.xingu.flow.BeforeExitState;
import br.com.ibnetwork.xingu.flow.BusinessFacade;
import br.com.ibnetwork.xingu.flow.engine.StateChangeException;

public class MyBusinessFacade
    implements BusinessFacade
{

	private String name;

	private Boolean cool;

	private Date currentDate;

	@BeforeExitState(state = MyWorkflow.GET_NAME)
	public boolean nameValidation()
	    throws StateChangeException
	{
		if (name == null || name.length() == 0)
		{
			name = null;
			return false;
		}
		return true;
	}

	@BeforeEnterState(state = MyWorkflow.GET_CURRENT_DATE)
	public boolean loadYesterdayDate()
	{
		if (currentDate == null)
		{
			Calendar dt = Calendar.getInstance();
			dt.add(Calendar.DAY_OF_YEAR, -1);
			currentDate = dt.getTime();
		}
		return true;
	}

	public Date getCurrentDate(){return currentDate;}
	public void setCurrentDate(Date dataAtual){this.currentDate = dataAtual;}
	public Boolean isCool(){return cool;}
	public void isCool(Boolean ehLegal){this.cool = ehLegal;}
	public String getName(){return name;}
	public void setName(String seuNome){this.name = seuNome;}
}
