package xavante.comet.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import xavante.comet.CometClient;
import xavante.comet.CometMessageFactory;
import xavante.comet.Registry;

public class RegistryImpl
	implements Registry
{

	private Map<String, CometClient>	clientById	= new HashMap<String, CometClient>();

	private synchronized String nextId()
	{
		String id = RandomStringUtils.randomAlphanumeric(CometMessageFactory.ID_LEN);
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