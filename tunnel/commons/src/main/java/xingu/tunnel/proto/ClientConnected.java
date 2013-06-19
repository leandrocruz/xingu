package xingu.tunnel.proto;

public class ClientConnected
	extends TcpMessageSupport
{
	public ClientConnected()
	{}

	public ClientConnected(int id)
	{
		super(id, null);
	}
}
