package br.com.ibnetwork.xingu.store;

import java.io.Serializable;


public interface PersistentBean
	extends Serializable
{
	long getId();
	
	void setId(long id);
}
