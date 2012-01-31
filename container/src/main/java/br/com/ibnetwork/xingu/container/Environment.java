package br.com.ibnetwork.xingu.container;

import java.util.Properties;

public interface Environment
{
	void put(String key, String value);
	
	String get(String key);
	
    void replaceVars(Properties props);
    
    String replaceVars(String var);
}
