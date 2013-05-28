package xingu.node.commons.signal.impl;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.commons.signal.ExceptionSignal;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.BehaviorPerformer;
import xingu.node.commons.signal.processor.SignalTask;
import br.com.ibnetwork.xingu.container.Inject;

public class SignalTaskImpl
	implements SignalTask
{
	@Inject
	protected BehaviorPerformer	performer;

	protected Signal			signal;

	protected Channel			channel;

	protected Logger			logger	= LoggerFactory.getLogger(this.getClass());

	public SignalTaskImpl(Signal signal, Channel pipe)
	{
		this.signal  = signal;
		this.channel = pipe;
	}

	@Override
	public Signal call()
		throws Exception
	{
		Signal reply = null;
		try
		{
			reply = performer.performBehavior(signal);
		}
		catch(Throwable t)
		{
			logger.error("Error processing signal " + signal, t);
			signal.setForward(false);
			reply = new ExceptionSignal(signal, t);
		}
		if(reply != null)
		{
			long signalId = signal.getSignalId();
			reply.setSignalId(signalId);
			channel.write(reply);
		}
		return reply;
	}
}