package br.com.ibnetwork.xingu.search;

public class SearchEngineException
    extends RuntimeException
{
    public SearchEngineException(String message, Throwable t)
    {
        super(message,t);
    }
}
