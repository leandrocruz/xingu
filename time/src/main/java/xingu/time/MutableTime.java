package xingu.time;


public interface MutableTime
    extends Time
{
    void advance(int quantity, int timeUnit);
    
    void set(int year, int month, int day);

    /**
     * Reset the clock so that it returns the system time on further calls to now()
     */
    void reset();
}
