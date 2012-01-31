package br.com.ibnetwork.xingu.network;

public interface AsyncEvent
{
    void waitWithoutInterruptions();

    boolean isDone();
}
