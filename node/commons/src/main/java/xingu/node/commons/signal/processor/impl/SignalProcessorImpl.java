package xingu.node.commons.signal.processor.impl;

import java.util.concurrent.Future;

import org.jboss.netty.channel.Channel;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.processor.SignalProcessor;

public class SignalProcessorImpl
	implements SignalProcessor
{

	@Override
	public Future<Signal> process(Signal signal, Channel channel)
		throws Exception
	{
		throw new NotImplementedYet();
	}

}
