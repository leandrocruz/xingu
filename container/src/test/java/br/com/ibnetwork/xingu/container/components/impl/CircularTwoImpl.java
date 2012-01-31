package br.com.ibnetwork.xingu.container.components.impl;

import org.apache.avalon.framework.activity.Initializable;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.components.CircularOne;
import br.com.ibnetwork.xingu.container.components.CircularTwo;

public class CircularTwoImpl
	implements CircularTwo, Initializable
{
	@Inject
	private CircularOne one;
	
	@Override
	public String two()
	{
		return "two + " + one.three();
	}

	@Override
	public void initialize()
		throws Exception
	{
		System.out.println("Initializing " + this.getClass().getSimpleName());
	}
}
