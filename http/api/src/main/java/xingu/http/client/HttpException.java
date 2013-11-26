package xingu.http.client;

public class HttpException
	extends RuntimeException
{
	public HttpException(Exception source)
	{
		super(source);
	}
}
