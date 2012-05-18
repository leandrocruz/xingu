package xingu.servlet.command.reply;

import xingu.servlet.command.CommandReply;
import xingu.servlet.command.CommandReplySupport;

public class SuccessReply
    extends CommandReplySupport
{
    public static final SuccessReply INSTANCE = new SuccessReply();
    
    SuccessReply()
    {}

    public static CommandReply instance()
    {
        return INSTANCE;
    }
}
