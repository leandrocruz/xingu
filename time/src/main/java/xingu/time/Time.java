package xingu.time;

public interface Time
{
    Instant now();
    
    void serverTime(long serverTime);

    long fix(long time);
}
