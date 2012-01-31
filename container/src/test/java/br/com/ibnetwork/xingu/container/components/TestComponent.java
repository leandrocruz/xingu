package br.com.ibnetwork.xingu.container.components;

import org.apache.avalon.framework.configuration.Configuration;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface TestComponent
{
    boolean isComposed();
    
    boolean isConfigured();
    
    boolean isInitialized();
    
    boolean isStarted();
    
    Configuration getConfiguration();
}
