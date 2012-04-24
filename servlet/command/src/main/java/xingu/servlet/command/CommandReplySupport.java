package xingu.servlet.command;

public class CommandReplySupport
    implements CommandReply
{
    protected String result = OK; /* used in json serialziation. Do not remove */
    
    @Override
    public String result()
    {
        return result;
    }
}
