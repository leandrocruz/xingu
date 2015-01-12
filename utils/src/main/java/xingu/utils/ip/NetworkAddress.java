package xingu.utils.ip;

import java.net.InetSocketAddress;

public class NetworkAddress
{
	private String mac;

	private String host;
	
	private int port;

	public NetworkAddress()
	{}

	public NetworkAddress(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
	
	public NetworkAddress(InetSocketAddress address, String mac)
	{
		host = address.getAddress().getHostAddress();
		port = address.getPort();
		this.mac = mac;
	}

	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " (" + host + ":" + port + ") #" + mac;
	}

	@Override
	public int hashCode()
	{
		return (host != null ? host.hashCode() : 0) + port + (mac != null ? mac.hashCode() : 0); 
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof NetworkAddress))
		{
			return false;
		}
		
		NetworkAddress other = (NetworkAddress) obj;
		return this.host == null ? other.host == null : host.equals(other.host)
				&& this.port == other.port
				&& this.mac == null ? other.mac == null : mac.equals(other.mac);
	}

	public String getMac() {return mac;}
	public void setMac(String mac) {this.mac = mac;}
	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
}
