package xingu.time.impl;

import java.util.Date;
import java.util.TimeZone;

import xingu.time.Instant;


public class InstantImpl
    implements Instant
{
    private long time;
    
    private String timeZone;
    
    private transient Date date;
    
    public InstantImpl()
    {
        time = System.currentTimeMillis();
        timeZone = TimeZone.getDefault().getID();
    }

    public InstantImpl(Date date)
    {
        this.date = date;
        this.time = date.getTime();
    }
    

    @Override
    public long time()
    {
        return time;
    }

    @Override
    public String timeZone()
    {
        return timeZone;
    }

    @Override
    public Date asDate()
    {
        if(date == null)
        {
            date = new Date(time);
        }
        return date;
    }

	@Override
	public String toString()
	{
		return asDate().toString();
	}
}
