package xingu.http.client;

public interface HttpProgressListener
{
	void on(long total, long progress)
		throws Exception;
}
