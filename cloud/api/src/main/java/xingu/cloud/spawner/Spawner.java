package xingu.cloud.spawner;

import java.util.List;

import org.jboss.netty.channel.Channel;

public interface Spawner
{
	List<Surrogate> list();
	
	Surrogate byChannel(Channel channel);

	List<Surrogate> spawn(SpawnRequest req)
		throws Exception;

	void release(Surrogate surrogate)
		throws Exception;

	Surrogate attach(String id, Channel channel);
	
	void dettach(Surrogate surrogate);
	
}