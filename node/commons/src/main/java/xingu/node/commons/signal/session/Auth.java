package xingu.node.commons.signal.session;


public class Auth
    extends HandShakeSignal
{
    private String contents;
    
    public Auth(String contents)
    {
        this.contents = contents;
    }

    public String contents()
    {
        return contents;
    }
}
