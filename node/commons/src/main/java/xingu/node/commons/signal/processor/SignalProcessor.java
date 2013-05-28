package xingu.node.commons.signal.processor;

import java.util.concurrent.Future;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;

public interface SignalProcessor
{
	Future<Signal> process(Signal signal, Channel channel)
		throws Exception;
}
