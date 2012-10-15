package br.com.ibnetwork.xingu.utils.ip;

import java.net.InetSocketAddress;

public class NetworkAddress
{
	private String hardwareAddress;

	private String host;
	
	private int port;

	public NetworkAddress(InetSocketAddress address, String mac)
	{
		host = address.getAddress().getHostAddress();
		port = address.getPort();
		hardwareAddress = mac;
	}

	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " (" + host + ":" + port + ") #" + hardwareAddress;
	}


	public String getHardwareAddress() {return hardwareAddress;}
	public void setHardwareAddress(String hardwareAddress) {this.hardwareAddress = hardwareAddress;}
	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
}
