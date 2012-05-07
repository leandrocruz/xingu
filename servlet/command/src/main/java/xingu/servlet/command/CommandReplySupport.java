package xingu.servlet.command;


public class CommandReplySupport
    implements CommandReply
{
    protected String result = OK; /* used in json serialziation. Do not remove */
	
    private long id;
    
    @Override
    public String result()
    {
        return result;
    }

	@Override
	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public long getId()
	{
		return id;
	}
}
