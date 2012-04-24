package xingu.servlet.container;

public interface ApplicationContext
{
    String name();

    String war();
    
    boolean isRunning();
    
    void stop()
        throws Exception;

    void start()
        throws Exception;

    String url();
}
