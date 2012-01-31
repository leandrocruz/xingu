package br.com.ibnetwork.xingu.network;

import org.apache.avalon.framework.activity.Startable;


public interface Server
    extends Attachable, Startable
{
    boolean isRunning();
}
