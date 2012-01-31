package br.com.ibnetwork.xingu.store;

public interface SnapshotListener
{
    void beforeSnapshot();

    void snapshotFailed(Exception e);

    void afterSnapshot();
}
