package xingu.http.client;

public interface RetryPolicy
{
    boolean retry(int result);
    RetryPolicy update(int result);

}
