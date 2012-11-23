package br.com.ibnetwork.xingu.utils.ip;

import java.net.InetSocketAddress;

public class NetworkAddress
{
	private String mac;

	private String host;
	
	private int port;

	public NetworkAddress()
	{}
	
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

	public String getMac() {return mac;}
	public void setMac(String mac) {this.mac = mac;}
	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
}
