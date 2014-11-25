package xingu.cloud.spawner;

import java.util.List;

import org.jboss.netty.channel.Channel;

public interface Spawner
{
	List<Surrogate> list();
	
	Surrogate byChannel(Channel channel);

	List<Surrogate> spawn(SpawnRequest req)
		throws Exception;

	Surrogate attach(String id, Channel channel);
	void dettach(Surrogate surrogate);
	
	void release(Surrogate surrogate, String bundle);

	@Deprecated
	List<Surrogate> get(int count, String bundle);
	
	@Deprecated
	int getLockCount(String bundle);

	@Deprecated
	int getSurrogateCount();
}
