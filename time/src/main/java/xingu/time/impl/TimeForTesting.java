package xingu.time.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.time.Instant;
import xingu.time.MutableTime;
import xingu.utils.TimeUtils;


public class TimeForTesting
    extends TimeSupport
    implements MutableTime, Configurable
{
    private Instant now;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        String start = conf.getChild("time").getAttribute("start", null);
        if(start != null)
        {
            try
            {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start);
                set(startDate);
            }
            catch (ParseException e)
            {
                logger.error("Error parsing time '"+start+"'", e);
            }
        }
    }

    @Override
    public Instant now()
    {
        if(now != null)
        {
            return now;
        }
        return new InstantImpl();
    }
    
    @Override
    public void reset()
    {
        now = null;
    }
    
    @Override
    public void advance(int quantity, int timeUnit)
    {
        Instant now = now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now.asDate());
        calendar.add(timeUnit, quantity);
        this.now = new InstantImpl(calendar.getTime());
    }

    public void rewind(int quantity, int timeUnit)
    {
        Instant now = now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now.asDate());
        calendar.add(timeUnit, -1 * quantity);
        this.now = new InstantImpl(calendar.getTime());
        
    }

    public void set(Date date)
    {
        now = new InstantImpl(date);
    }

    @Override
    public void set(int year, int month, int day)
    {
        now = new InstantImpl(TimeUtils.date(year, month, day).getTime());
    }
}
