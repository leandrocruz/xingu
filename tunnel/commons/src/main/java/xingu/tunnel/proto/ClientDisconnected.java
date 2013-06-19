package xingu.tunnel.proto;

public class ClientDisconnected
	extends TcpMessageSupport
{
	public ClientDisconnected()
	{}

	public ClientDisconnected(int id)
	{
		super(id, null);
	}
}
