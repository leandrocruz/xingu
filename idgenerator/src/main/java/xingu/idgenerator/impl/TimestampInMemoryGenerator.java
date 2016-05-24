package xingu.idgenerator.impl;

import java.util.HashSet;
import java.util.Set;

import xingu.idgenerator.GeneratorException;
import xingu.utils.RandomIdGenerator;

public class TimestampInMemoryGenerator
	extends GeneratorSupport<String>
{
	private Set<String>	set			= new HashSet<String>();

	private int			suffixSize	= 6;

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
		String result = RandomIdGenerator.next(suffixSize);
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
