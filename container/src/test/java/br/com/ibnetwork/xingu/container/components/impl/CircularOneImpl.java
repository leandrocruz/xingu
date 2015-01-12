package br.com.ibnetwork.xingu.container.components.impl;

import org.apache.avalon.framework.activity.Initializable;

import xingu.container.Inject;
import br.com.ibnetwork.xingu.container.components.CircularOne;
import br.com.ibnetwork.xingu.container.components.CircularTwo;

public class CircularOneImpl
	implements CircularOne, Initializable
{
	@Inject(lazy=true)
	private CircularTwo two;
	
	@Override
	public String one()
	{
		return "one + " + two.two();
	}

	@Override
	public String three()
	{
		return "3";
	}

	@Override
	public void initialize()
		throws Exception
	{
		System.out.println("Initializing " + this.getClass().getSimpleName());
	}
}
