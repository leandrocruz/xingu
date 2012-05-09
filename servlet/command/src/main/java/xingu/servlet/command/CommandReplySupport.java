package xingu.servlet.command;


public class CommandReplySupport
    implements CommandReply
{
    private long id;
    
    private boolean ok = true;
    
    private Throwable error;

	@Override public void setId(long id) {this.id = id;}
	@Override public long getId() {return id;}
	@Override public boolean isOk() {return ok;}
	@Override public void setOk(boolean ok) {this.ok = ok;}
	@Override public Throwable getError() {return error;}
	@Override public void setError(Throwable error){this.error = error;}
}
