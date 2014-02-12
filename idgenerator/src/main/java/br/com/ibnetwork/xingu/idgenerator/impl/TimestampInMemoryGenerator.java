package br.com.ibnetwork.xingu.idgenerator.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import xingu.time.Time;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.idgenerator.GeneratorException;

public class TimestampInMemoryGenerator
	extends GeneratorSupport<String>
{
	
	@Inject
	private Time					time;

	private Set<String>				set		= new HashSet<String>();

	private static final DateFormat	format	= new SimpleDateFormat("yyyMMdd.HHmmss");

	public TimestampInMemoryGenerator(String generatorId, int grabSize)
	{
		super(generatorId, grabSize);
	}

	@Override
	protected synchronized String increment(String lastUsed)
		throws GeneratorException
	{
		Date   now    = time.now().asDate();
		String result = format.format(now) + "-" + RandomStringUtils.randomAlphanumeric(16);

		if(set.contains(result))
		{
			return next();
		}

		set.add(result);
		return result;
	}

	@Override
	protected String loadInitialState()
		throws GeneratorException
	{
		return "";
	}

	@Override
	protected void updateState(String state)
		throws GeneratorException
	{}
}
