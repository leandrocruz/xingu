package xingu.store.impl.mybatis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlDateInterval
{
	public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final Date start;
	
	private final Date end;
	
	public SqlDateInterval(Date start, Date end)
	{
		this.start = start;
		this.end = end;
	}

	public String getStart()
	{
		return df.format(start);
	}
	
	public String getEnd()
	{
		return df.format(end);
	}
}
