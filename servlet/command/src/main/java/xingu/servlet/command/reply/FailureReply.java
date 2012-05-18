package xingu.servlet.command.reply;

import xingu.servlet.command.CommandReplySupport;

public class FailureReply
    extends CommandReplySupport
{
    private final String reason;
    
    public FailureReply(String string)
    {
    	setOk(false);
        this.reason = string;
    }

    public String getReason()
    {
        return reason;
    }
}
