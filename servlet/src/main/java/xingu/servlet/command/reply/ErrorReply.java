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
        this.result = ERROR;
        this.reason = reason;
    }
    
    public String reason()
    {
        return reason;
    }
}
