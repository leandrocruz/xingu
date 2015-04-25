package xingu.node.traffic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Date;

import org.jboss.netty.channel.Channel;
import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.node.traffic.ChannelData;
import xingu.node.traffic.TrafficHandler;
import xingu.time.Time;
import xingu.time.impl.InstantImpl;
import xingu.utils.TimeUtils;

public class TrafficHandlerTest
	extends XinguTestCase
{
	@Inject
	private Time time;

	@Inject
	private TrafficHandler traffic;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		withMock(Time.class);
	}

	@Test
	public void testStats()
		throws Exception
	{
		Date created = TimeUtils.date(2000, 0, 1).getTime();
		Date write   = TimeUtils.date(2000, 0, 2).getTime();
		Date msg     = TimeUtils.date(2000, 0, 3).getTime();
		when(time.now()).thenReturn(
				new InstantImpl(created),
				new InstantImpl(write),
				new InstantImpl(msg));
		
		Channel channel = mock(Channel.class);
		when(channel.getId()).thenReturn(1);

		Collection<ChannelData> coll = null;
		
		traffic.onConnect(channel);
		coll = traffic.getData();
		assertEquals(1, coll.size());

		ChannelData data = get(coll, 0);
		assertSame(channel, data.getChannel());
		assertEquals(created.getTime(), data.timeForLastEvent());
		
		traffic.onWrite(channel);
		assertEquals(write.getTime(), data.timeForLastEvent());

		traffic.onMessage(channel);
		assertEquals(msg.getTime(), data.timeForLastEvent());

		traffic.onDisconnect(channel);
		coll = traffic.getData();
		assertEquals(0, coll.size());
	}

	private ChannelData get(Collection<ChannelData> coll, int pos)
	{
		return coll.toArray(new ChannelData[]{})[pos];
	}
}
