package xavante.comet.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.commons.lang3.RandomStringUtils;

import xavante.comet.CometClient;
import xavante.comet.MessageFactory;
import xavante.comet.Registry;
import br.com.ibnetwork.xingu.container.Inject;

public class RegistryImpl
	implements Registry, Initializable
{
	@Inject
	private MessageFactory				factory;

	private Map<String, CometClient>	clientById	= new HashMap<String, CometClient>();

	private int							idLength;

	@Override
	public void initialize()
		throws Exception
	{
		idLength = factory.getMessageIdLength();
	}

	private synchronized String nextId()
	{
		
		String id = RandomStringUtils.randomAlphanumeric(idLength);
		if(!clientById.containsKey(id))
		{
			return id;
		}
		return nextId();
	}

	@Override
	public boolean has(String id)
	{
		return clientById.containsKey(id);
	}

	@Override
	public CometClient byId(String id)
	{
		return clientById.get(id);
	}

	@Override
	public CometClient newClient()
	{
		String      id     = nextId();
		CometClient result = new CometClient(id);
		clientById.put(id, result);
		return result;
	}
}