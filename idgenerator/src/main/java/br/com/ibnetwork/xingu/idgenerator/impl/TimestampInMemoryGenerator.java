package br.com.ibnetwork.xingu.idgenerator.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import xingu.time.Time;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.idgenerator.GeneratorException;
import br.com.ibnetwork.xingu.utils.DateFormats;

public class TimestampInMemoryGenerator
	extends GeneratorSupport<String>
{
	@Inject
	private Time					time;

	private Set<String>				set		= new HashSet<String>();
	
	private int suffixSize = 6;

	public TimestampInMemoryGenerator()
	{
		super("", -1);
	}

	public TimestampInMemoryGenerator(int suffixSize)
	{
		super("", -1);
		this.suffixSize = suffixSize;
	}

	public TimestampInMemoryGenerator(String generatorId, int grabSize)
	{
		super(generatorId, grabSize);
	}

	@Override
	protected synchronized String increment(String lastUsed)
		throws GeneratorException
	{
		Date   now    = time.now().asDate();
		String result = DateFormats.yyyyMMdd_HHmmss.format(now) + "-" + RandomStringUtils.randomAlphanumeric(suffixSize).toLowerCase();

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
