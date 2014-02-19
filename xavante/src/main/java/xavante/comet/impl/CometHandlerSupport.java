package xavante.comet.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;

import xavante.comet.CometClient;
import xavante.comet.CometHandler;
import xavante.comet.CometMessage;
import xavante.comet.Registry;
import xingu.codec.Codec;
import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.BehaviorPerformer;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public abstract class CometHandlerSupport
	implements CometHandler
{
	@Inject("xavante")
	protected Codec				codec;

	@Inject
	protected Registry			registry;

	@Inject
	protected BehaviorPerformer	performer;

	@Override
	public String onMessage(CometMessage msg)
		throws Exception
	{
		String cmd = msg.getCommand();
		if("con".equalsIgnoreCase(cmd))
		{
			return connect(msg);
		}
		else if("drn".equalsIgnoreCase(cmd))
		{
			return drain(msg);
		}
		else if("snd".equalsIgnoreCase(cmd))
		{
			return send(msg);
		}
		else
		{
			throw new NotImplementedYet("Can't execute '" + cmd + "'");
		}
	}

	private String connect(CometMessage msg)
		throws Exception
	{
		CometClient client = registry.newClient();
		return "{\"type\": \"OK\", \"sessionId\":\"" + client.id() + "\"}";
	}

	private String send(CometMessage msg)
		throws Exception
	{
		/*
		 * TODO: issue #354 not all messages should have waiters. Free
		 * threads/resources asap
		 */
		String seq   = msg.getSequence();
		String token = msg.getToken();
		String data  = msg.getData();

		Object decoded = codec.decode(data);
		if(decoded instanceof Signal)
		{
			Signal      signal         = (Signal) decoded;
			String        id             = signal.getSignalId();
			boolean     processEnabled = signal.isProcessEnabled();
			Identity<?> owner          = findOwner(token, signal);

			if(owner == null && !processEnabled)
			{
				/* All signals require an owner, except for Login */
				throw new NotImplementedYet("Owner is null on signal: '" + signal + "'");
			}

			signal.setOwner(owner);
			verifyOwnership(signal);

			Signal reply = performer.performBehavior(signal, null);
			if(reply != null)
			{
				reply.setSignalId(id);
				String encoded = codec.encode(reply);
				return encoded;
			}
			else
			{
				return "{}";
			}
		}
		else
		{
			throw new NotImplementedYet("'" + data + "' is not a signal");
		}
	}

	protected abstract void verifyOwnership(Signal signal);

	protected abstract Identity<?> findOwner(String token, Signal signal);

	private String drain(CometMessage msg)
	{
		String      hash     = msg.getToken();
		CometClient client   = registry.byId(hash);
		String[]    messages = client.drain();
		if(messages == null)
		{
			// Thread interrupted
			return null;
		}
		StringBuffer sb = toString(messages);
		return sb.toString();
	}

	private StringBuffer toString(String[] messages)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("{\"len\":").append(messages.length).append(", \"data\": [");

		int i = 0;
		for(String msg : messages)
		{
			i++;
			sb.append(msg);
			if(i < messages.length)
			{
				sb.append(", ");
			}
		}
		sb.append("]}");
		return sb;
	}

	@Override
	public String onError(Throwable t)
	{
		String trace = ExceptionUtils.getStackTrace(t);
		return trace;
		// TODO://
		// String encoded = codec.encode(new ErrorReply(t, "Error handling message", trace));
		// return encoded;
	}
}