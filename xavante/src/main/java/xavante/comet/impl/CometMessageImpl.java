package xavante.comet.impl;

import java.net.SocketAddress;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import xavante.comet.CometMessage;

public class CometMessageImpl
	implements CometMessage
{
	private String			token;

	private String			sequence;

	private String			command;

	private String			data;

	private HttpRequest		request;

	private HttpResponse	response;

	private SocketAddress	localAddress;

	private SocketAddress	remoteAddress;
	
	public String getToken(){return token;}
	public void setToken(String hash){this.token = hash;}
	public String getSequence(){return sequence;}
	public void setSequence(String sequence){this.sequence = sequence;}
	public String getCommand(){return command;}
	public void setCommand(String command){this.command = command;}
	public String getData(){return data;}
	public void setData(String data){this.data = data;}
	public HttpRequest getRequest(){return request;}
	public void setRequest(HttpRequest req){this.request = req;}
	public HttpResponse getResponse(){return response;}
	public void setResponse(HttpResponse resp){this.response = resp;}
	public SocketAddress getLocalAddress(){return localAddress;}
	public void setLocalAddress(SocketAddress localAddress){this.localAddress = localAddress;}
	public SocketAddress getRemoteAddress(){return remoteAddress;}
	public void setRemoteAddress(SocketAddress remoteAddress){this.remoteAddress = remoteAddress;}
}