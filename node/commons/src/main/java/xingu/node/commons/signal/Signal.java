package xingu.node.commons.signal;

import java.io.Serializable;

import xingu.node.commons.identity.Identity;
import br.com.ibnetwork.xingu.utils.ip.NetworkAddress;

public interface Signal
	extends Serializable
{
	String getSignalId();
	void setSignalId(String id);

	long getSessionId();
	void setSessionId(long sessionId);

	Identity<?> getIdentity();
	void setIdentity(Identity<?> identity);

	Identity<?> getOwner();
	void setOwner(Identity<?> owner);

	long cTime(); /* Client Time */
	void cTime(long time);

	String cTimeZone();
	void cTimeZone(String timeZone);

	long sTime(); /* Server Time */
	void sTime(long time);

	NetworkAddress getAddress();
	void setAddress(NetworkAddress address);

	boolean isForward();
	void setForward(boolean forward);

	boolean isSync();
	boolean isProcessEnabled();
}