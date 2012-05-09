package xingu.servlet.command.reply;

import xingu.servlet.command.CommandReplySupport;

public class ErrorReply
    extends CommandReplySupport
{
    private String reason;

    ErrorReply()
    {}
    
    public ErrorReply(String reason)
    {
    	setOk(false);
    	this.reason = reason;
    }
    
    public String getReason()
    {
        return reason;
    }
}
