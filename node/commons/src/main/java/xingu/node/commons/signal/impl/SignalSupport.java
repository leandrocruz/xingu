package xingu.node.commons.signal.impl;

import xingu.node.commons.identity.Identity;
import xingu.node.commons.signal.Signal;
import br.com.ibnetwork.xingu.utils.ip.NetworkAddress;

public class SignalSupport
	implements Signal
{
	protected long				signalId;

	protected Identity<?>		owner;

	protected Identity<?>		identity;

	protected long				sessionId;

	protected long				cTime;

	protected String			cTimeZone;

	protected long				sTime;

	protected NetworkAddress	address;

	protected boolean			forward			= true;

	protected boolean			sync			= false;

	protected boolean			processEnabled	= true;

    /* @formatter:off */
	@Override public long getSignalId() {return signalId;}
	@Override public void setSignalId(long id){this.signalId = id;}
	@Override public Identity<?> getIdentity(){return identity;}
	@Override public void setIdentity(Identity<?> identity){this.identity = identity;}
	@Override public Identity<?> getOwner() {return owner;}
	@Override public void setOwner(Identity<?> owner){this.owner = owner;}
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
    /* @formatter:on */

}
