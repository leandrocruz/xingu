package br.com.ibnetwork.xingu.flow;

public interface PersistenceAgent 
{

	void persist(String key, Object toPersist);
	
	Object load(String key);

}
