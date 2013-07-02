package xingu.node.commons.signal;

import br.com.ibnetwork.xingu.utils.ip.NetworkAddress;

public class SignalSupport
	implements Signal
{
	private long				signalId;

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
