package xingu.exception;

public interface ExceptionHandler
{
    void handle(Throwable throwable);

    void handle(Throwable throwable, Thread thread);
}
