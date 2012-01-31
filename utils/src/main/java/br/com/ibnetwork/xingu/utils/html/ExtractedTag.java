package br.com.ibnetwork.xingu.utils.html;

public class ExtractedTag
{
    private String tag;
    
    private int start;
    
    private int end;
    
    public ExtractedTag(String tag, int start, int end)
    {
        this.tag = tag;
        this.start = start;
        this.end = end;
    }

    public String name()
    {
        return tag;
    }
    
    public int start()
    {
        return start;
    }
    
    public int end()
    {
        return end;
    }
}
