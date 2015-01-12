package xingu.servlet.command;

import java.io.Serializable;

import xingu.lang.WithId;

public interface CommandReply
    extends Serializable, WithId
{
    boolean isOk();
    void setOk(boolean ok);
    
    Throwable getError();
    void setError(Throwable error);
}
