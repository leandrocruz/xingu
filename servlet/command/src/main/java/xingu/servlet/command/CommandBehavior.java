package xingu.servlet.command;


public interface CommandBehavior
{
    CommandReply perform(Command cmd)
        throws Exception;
}
