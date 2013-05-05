package xingu.node.commons.signal;

import java.io.Serializable;

import br.com.ibnetwork.xingu.utils.ip.NetworkAddress;

public interface Signal
	extends Serializable
{
	long getSignalId();
    void setSignalId(long id);
    
    String getSessionId();
    void setSessionId(String sessionId);
    
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