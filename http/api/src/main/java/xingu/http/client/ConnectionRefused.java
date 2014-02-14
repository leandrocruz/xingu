package xingu.http.client;

public class ConnectionRefused
	extends HttpException
{
	public ConnectionRefused()
	{
		super();
	}
	
	public ConnectionRefused(String msg)
	{
		super(msg);
	}
}
