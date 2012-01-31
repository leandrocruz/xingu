package xingu.time;

import java.io.Serializable;
import java.util.Date;

public interface Interval
	extends Serializable
{
	Date getStart();
	
	Date getEnd();
	
}
