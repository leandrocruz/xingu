package xavante.comet;

import org.jboss.netty.handler.codec.http.HttpResponse;

import xavante.XavanteRequest;

public interface MessageFactory
{
	CometMessage build(XavanteRequest xeq, HttpResponse resp)
		throws Exception;

	int getMessageIdLength();
}
