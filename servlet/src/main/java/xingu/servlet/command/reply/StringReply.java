package xingu.servlet.command.reply;

import xingu.servlet.command.CommandReplySupport;

public class StringReply
    extends CommandReplySupport
{
    private final String string;
    
    public StringReply(String string)
    {
        this.string = string;
    }

    public String string()
    {
        return string;
    }
}
