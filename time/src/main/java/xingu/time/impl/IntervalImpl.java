package xingu.time.impl;

import java.util.Date;

import xingu.time.Interval;

public class IntervalImpl
	implements Interval
{
	private final Date start;
	private final Date end;

	public IntervalImpl(Date start, Date end)
	{
		this.start = start;
		this.end = end;
	}
	
	@Override
	public Date getStart()
	{
		return start;
	}

	@Override
	public Date getEnd()
	{
		return end;
	}
}
