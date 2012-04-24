package xingu.servlet.command.reply;

import xingu.servlet.command.CommandReplySupport;

public class RedirectUrlReply
    extends CommandReplySupport
{
    private final String url;

    public RedirectUrlReply(String url)
    {
        this.url = url;
    }
    
    public String url()
    {
        return url;
    }
}
