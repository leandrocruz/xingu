package xingu.servlet.command;

import java.io.Serializable;

import xingu.lang.WithId;

/**
 * Don't remove this interface. This is required to speed-up the gwt compilation process
 *   
 * @see http://efreedom.com/Question/1-3059787/Gwt-Using-List-Serializable-RPC-Call
 */
public interface Command
    extends Serializable, WithId
{
	String getChannel();
	void setChannel(String channel);
}
