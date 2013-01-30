package xingu.journal;

import java.util.Stack;

public class PatternInterpolator
{
	public static final char ESCAPE = '\\';
	public static final char START 	= '{';
	public static final char END 	= '}';
	
    public static String interpolate(String pattern, Object... values)
    {
    	int count = 0;
        char[] array = pattern.toCharArray();
        Stack<Region> stack = new Stack<Region>();
        stack.push(new Region(-1, 0));
        for(int i = 0 ; i < array.length ; i++)
        {
            char c = array[i];
            char prev = i > 0 ? array[i-1] : '$'; 
            //char next = i+1 < array.length ? array[i+1] : 0;
            
            if(c == START && prev != ESCAPE)
            {
                stack.push(new Region(count++, i));
                continue;
            }
            
            if(c == END && prev != ESCAPE)
            {
                Region region = stack.pop();
                int index = region.index();
                String value = values[index].toString();
                region.value = value;
                
                Region peek = stack.peek();
                if(peek != null)
                {
                	String s = region.value();
                    peek.buffer.append(s);
                }
            }
            else
            {
                stack.peek().buffer.append(c);
            }
        }
        return stack.peek().buffer.toString();
    }

}

class Region
{
	String value;

	final int index;

	final int start;
    
    StringBuffer buffer = new StringBuffer();

    
    public Region(int index, int start)
    {
        this.index = index;
		this.start = start;
    }

	public String value()
	{
		return value;
	}

	public int index()
	{
		return index;
	}

	@Override
    public String toString()
    {
        String value = buffer.toString(); 
        return "("+start+") "+ value;
    }
}