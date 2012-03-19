package xingu.email.impl;

public class FindEmailParameters
{
    private final String to;

    private final String typeLike;

    public FindEmailParameters(String to, String typeLike)
    {
        this.to = to;
        this.typeLike = typeLike;
    }

    public String getTo()
    {
        return to;
    }

    public String getTypeLike()
    {
        return typeLike;
    }
}
