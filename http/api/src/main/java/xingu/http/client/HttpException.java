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

	public HttpException(Throwable t)
	{
		super(t);
	}

	public HttpException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
