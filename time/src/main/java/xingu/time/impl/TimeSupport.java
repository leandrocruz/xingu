package xingu.time.impl;

import xingu.time.Time;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public abstract class TimeSupport
    implements Time
{
    @Override
    public void serverTime(long serverTime)
    {
        throw new NotImplementedYet("TODO");
    }

    @Override
    public long fix(long localTime)
    {
        throw new NotImplementedYet("TODO");
    }
}
