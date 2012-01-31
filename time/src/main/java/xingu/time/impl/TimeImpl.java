package xingu.time.impl;

import xingu.time.Instant;

public class TimeImpl
    extends TimeSupport
{
    private long offset;

    @Override
    public Instant now()
    {
        return new InstantImpl();
    }

    @Override
    public long fix(long time)
    {
        long result = time - offset;
        return result;
    }

    @Override
    public void serverTime(long serverTime)
    {
        offset = System.currentTimeMillis() - serverTime;                
    }
}
