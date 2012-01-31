/**
 * 
 */
package br.com.ibnetwork.xingu.flow.test.simple;

import java.util.HashMap;

import br.com.ibnetwork.xingu.flow.PersistenceAgent;

public class MyPersistenceAgent implements PersistenceAgent {
	HashMap<String , Object> hash = new HashMap<String , Object>();

	public Object load(String key) {
		return hash.get(key);
	}

	public void persist(String key, Object toPersist) {
		hash.put(key, toPersist);				
	}
}