package xingu.http.client;

public class HttpException
	extends RuntimeException
{
	public HttpException()
	{
		super();
	}

	public HttpException(String msg)
	{
		super(msg);
	}

	public HttpException(Exception source)
	{
		super(source);
	}

	public HttpException(String msg, Exception source)
	{
		super(msg, source);
	}
}
