package xingu.node.traffic.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;

import xingu.container.Inject;
import xingu.node.traffic.ChannelData;
import xingu.node.traffic.TrafficHandler;
import xingu.time.Time;

public class TrafficHandlerImpl
	implements TrafficHandler
{
	@Inject
	private Time time;
	
	private Map<Integer, ChannelData> dataByChannelId = new HashMap<>();
	
	@Override
	public void onConnect(Channel channel)
	{
		long now = time.now().time();
		Integer id = channel.getId();
		dataByChannelId.put(id, new ChannelDataImpl(channel, now));
	}

	@Override
	public void onDisconnect(Channel channel)
	{
		Integer id = channel.getId();
		dataByChannelId.remove(id);
	}

	@Override
	public void onWrite(Channel channel)
	{
		touch(channel);
	}

	@Override
	public void onMessage(Channel channel)
	{
		touch(channel);
	}

	@Override
	public Collection<ChannelData> getData()
	{
		Collection<ChannelData> values = dataByChannelId.values();
		return Collections.unmodifiableCollection(values);
	}

	private ChannelDataImpl get(Channel channel)
	{
		Integer id = channel.getId();
		ChannelDataImpl data = (ChannelDataImpl) dataByChannelId.get(id);
		return data;
	}

	private void touch(Channel channel)
	{
		long now = time.now().time();
		ChannelDataImpl data = get(channel);
		if(data != null)
		{
			data.setLast(now);
		}
	}

	@Override
	public ChannelData byChannel(int id)
	{
		return dataByChannelId.get(id);
	}
}