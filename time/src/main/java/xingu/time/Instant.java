package xingu.time;

import java.util.Date;

public interface Instant
{
    long time();
    
    String timeZone();

    Date asDate();
}
