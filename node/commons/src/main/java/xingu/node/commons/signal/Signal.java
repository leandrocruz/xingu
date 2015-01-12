package xingu.node.commons.signal;

import java.io.Serializable;

import xingu.node.commons.identity.Identity;
import xingu.utils.ip.NetworkAddress;

public interface Signal<T>
	extends Serializable
{
	String getSignalId();
	void setSignalId(String id);

	long getSessionId();
	void setSessionId(long sessionId);

	Identity<T> getIdentity();
	void setIdentity(Identity<T> identity);

	Identity<T> getOwner();
	void setOwner(Identity<T> owner);

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
	
	boolean isLate();
	void setLate(boolean late);
}