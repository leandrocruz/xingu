package xingu.node.commons.signal.impl;

import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;
import xingu.utils.ip.NetworkAddress;

public class SignalSupport<T>
	implements Signal<T>
{
	protected String			signalId;

	protected Identity<T>		owner;

	protected Identity<T>		identity;

	protected long				sessionId;

	protected long				cTime;

	protected String			cTimeZone;

	protected long				sTime;

	protected NetworkAddress	address;

	protected boolean			forward			= true;

	protected boolean			sync			= false;

	protected boolean			processEnabled	= true;

	private boolean	late;

    /* @formatter:off */
	@Override public String getSignalId() {return signalId;}
	@Override public void setSignalId(String id){this.signalId = id;}
	@Override public Identity<T> getIdentity(){return identity;}
	@Override public void setIdentity(Identity<T> identity){this.identity = identity;}
	@Override public Identity<T> getOwner() {return owner;}
	@Override public void setOwner(Identity<T> owner){this.owner = owner;}
    @Override public long getSessionId() {return sessionId;}
    @Override public void setSessionId(long sessionId) {this.sessionId = sessionId;}
    @Override public long cTime() {return cTime;}
    @Override public void cTime(long time){this.cTime = time;}
    @Override public long sTime() {return sTime;}
    @Override public void sTime(long time){this.sTime = time;}
    @Override public String cTimeZone() {return cTimeZone;}
    @Override public void cTimeZone(String timeZone) {this.cTimeZone = timeZone;}
    @Override public boolean isForward() {return forward;}
    @Override public void setForward(boolean forward) {this.forward = forward;}
    @Override public NetworkAddress getAddress() {return address;}
    @Override public void setAddress(NetworkAddress address) {this.address = address;}
    @Override public boolean isSync() {return sync;}
    @Override public boolean isProcessEnabled() {return processEnabled;}
    @Override public boolean isLate(){return late;}
    @Override public void setLate(boolean late){this.late = late;}
    /* @formatter:on */
}
